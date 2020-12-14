package com.example.garam.takemehome_android.restaurant.order

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
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
 //   private lateinit var orderListRecycler : OrderListViewAdapter
    private lateinit var root : View
    private lateinit var sharedViewModel : RestaurantSharedViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        sharedViewModel = ViewModelProvider(requireActivity()).get(RestaurantSharedViewModel::class.java)

        val restaurantId = sharedViewModel.getId()
        sharedViewModel.setId(restaurantId!!)


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

                      //  orderListRecycler.notifyDataSetChanged()

                    }

                    "주문 조회 실패" -> {

                    }
                }
            }
        })
    }


    private fun requestDeliveryMethod(orderId: Int, deliveryInfo: JsonObject){
        networkService.requestDelivery(orderId,deliveryInfo).enqueue(object : Callback<JsonObject>{
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {

            }

            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {

            }
        })
    }

}