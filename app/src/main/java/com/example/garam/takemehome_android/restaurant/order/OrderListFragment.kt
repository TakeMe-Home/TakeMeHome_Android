package com.example.garam.takemehome_android.restaurant.order

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.garam.takemehome_android.R
import com.example.garam.takemehome_android.network.NetworkController
import com.example.garam.takemehome_android.network.NetworkServiceRestaurant
import com.example.garam.takemehome_android.restaurant.RestaurantSharedViewModel
import com.example.garam.takemehome_android.restaurant.receipt.ReceiptMenuList
import com.google.gson.JsonObject
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OrderListFragment : Fragment() {

    private val networkService: NetworkServiceRestaurant by lazy {
        NetworkController.instance.networkServiceRestaurant
    }

    private val lists = arrayListOf<OrderList>()
    private val menuList = arrayListOf<ReceiptMenuList>()
    private lateinit var root : View
    private lateinit var sharedViewModel : RestaurantSharedViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        sharedViewModel = ViewModelProvider(requireActivity()).get(RestaurantSharedViewModel::class.java)

        val restaurantId = sharedViewModel.getId()
        sharedViewModel.setId(restaurantId!!)
        Log.e("오더 ",restaurantId.toString())
        root = inflater.inflate(R.layout.fragment_order_list, container, false)

        findAllOrder(restaurantId)

        return root
    }

    private fun findAllOrder(restaurantId : Int){
        networkService.findOrder(restaurantId).enqueue(object : Callback<JsonObject> {
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {

            }

            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                val res = response.body()
                when(res?.get("message")?.asString){
                    "주문 조회 성공" -> {
                        val data = res.getAsJsonObject("data")
                        val dataObject = JSONObject(data.toString())
                        val orderResponse = dataObject.getJSONArray("orderFindResponses")
                        for (i in 0 until orderResponse.length()){
                            val totalPrice = orderResponse.getJSONObject(i).get("totalPrice")
                            val menuCounts = orderResponse.getJSONObject(i).getJSONObject("menuNameCounts")
                            val menuDetailCounts = menuCounts.getJSONArray("menuNameCounts")

                            for (j in 0 until menuDetailCounts.length()){
                                menuList.add(
                                    ReceiptMenuList(
                                        menuDetailCounts.getJSONObject(i).get("name").toString(),
                                        menuDetailCounts.getJSONObject(i).get("count").toString()
                                            .toInt()
                                    )
                                )
                            }
                            val paymentType = orderResponse.getJSONObject(i).get("paymentType")
                         //   lists.add(OrderList(paymentType.toString(),totalPrice.toString().toInt()
                        //        ,menuList))
                        }

                      //  receiptRecycler.notifyDataSetChanged()


                    }

                    "주문 조회 실패" -> {

                    }
                }
            }
        })
    }
}