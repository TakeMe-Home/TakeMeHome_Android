package com.example.garam.takemehome_android.restaurant.order

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.garam.takemehome_android.R
import com.example.garam.takemehome_android.network.NetworkController
import com.example.garam.takemehome_android.network.NetworkServiceRestaurant
import com.example.garam.takemehome_android.restaurant.AllOrderListDataClass
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

    private val lists = arrayListOf<AllOrderListDataClass>()
    private lateinit var orderListRecycler : OrderListViewAdapter
    private lateinit var root : View
    private lateinit var sharedViewModel : RestaurantSharedViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        sharedViewModel = ViewModelProvider(requireActivity()).get(RestaurantSharedViewModel::class.java)
        root = inflater.inflate(R.layout.fragment_order_list, container, false)

        val recycler = root.findViewById<RecyclerView>(R.id.orderListRecyclerView)
        val restaurantId = sharedViewModel.getId()
        sharedViewModel.setId(restaurantId!!)


        findAllOrder(restaurantId)

        orderListRecycler = OrderListViewAdapter(lists,root.context){
            orderList ->
        }

        recycler.adapter = orderListRecycler
        recycler.layoutManager = LinearLayoutManager(root.context)


        return root
    }

    private fun findAllOrder(restaurantId : Int){
        val errorMessage = Toast.makeText(root.context,"주문 조회에 실패했습니다", Toast.LENGTH_SHORT)

        networkService.findOrder(restaurantId).enqueue(object : Callback<JsonObject> {
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                errorMessage.show()
            }

            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                val res = response.body()
                when(res?.get("message")?.asString){
                    "주문 조회 성공" -> {
                        val data = res.getAsJsonObject("data")
                        val dataObject = JSONObject(data.toString())
                        val orderResponse = dataObject.getJSONArray("orderFindResponses")

                        for (i in 0 until orderResponse.length()) {
                            val orderStatus =
                                orderResponse.getJSONObject(i).getString("orderStatus")
                            val totalPrice = orderResponse.getJSONObject(i).getInt("totalPrice")
                            val orderDelivery =
                                orderResponse.getJSONObject(i).getJSONObject("orderDelivery")
                            val customerAddress = orderDelivery.getString("address")
                            val deliveryStatus = orderDelivery.getString("status")
                            val orderId = orderResponse.getJSONObject(i).getInt("orderId")
                            val paymentType =
                                orderResponse.getJSONObject(i).getString("paymentType")
                            val paymentStatus =
                                orderResponse.getJSONObject(i).getString("paymentStatus")

                            val customerInfo =
                                orderResponse.getJSONObject(i).getJSONObject("orderCustomer")
                            val customerPhone = customerInfo.getString("phoneNumber")

                            when{
                                !(deliveryStatus == "NONE" || deliveryStatus == "REQUEST")-> {
                                    lists.add(AllOrderListDataClass(
                                            customerAddress, customerPhone, totalPrice, paymentType
                                            , paymentStatus, orderId)
                                    )
                                }
                            }
                        }
                        orderListRecycler.notifyDataSetChanged()
                    }

                    "주문 조회 실패" -> {
                        errorMessage.show()
                    }
                }
            }
        })
    }


}