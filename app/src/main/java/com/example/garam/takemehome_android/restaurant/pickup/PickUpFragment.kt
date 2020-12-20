package com.example.garam.takemehome_android.restaurant.pickup

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.garam.takemehome_android.R
import com.example.garam.takemehome_android.network.NetworkController
import com.example.garam.takemehome_android.network.NetworkServiceRestaurant
import com.example.garam.takemehome_android.restaurant.RestaurantSharedViewModel
import com.google.gson.JsonObject
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PickUpFragment : Fragment() {

    private val networkService: NetworkServiceRestaurant by lazy {
        NetworkController.instance.networkServiceRestaurant
    }
    private lateinit var sharedViewModel : RestaurantSharedViewModel
    private lateinit var root : View
    private val lists = arrayListOf<PickUpWaitingList>()
    private lateinit var pickUpRecycler : PickUpWaitListViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        root = inflater.inflate(R.layout.fragment_pick_up, container, false)

        val recycler  = root.findViewById<RecyclerView>(R.id.pickUpRecyclerVIew)

        sharedViewModel = ViewModelProvider(requireActivity()).get(RestaurantSharedViewModel::class.java)

        val restaurantId = sharedViewModel.getId()
        sharedViewModel.setId(restaurantId!!)

        waitForPickUpOrder(restaurantId)

        pickUpRecycler = PickUpWaitListViewAdapter(lists,root.context){
            pickUpWaitingList ->

        }

        recycler.adapter = pickUpRecycler
        recycler.layoutManager = LinearLayoutManager(root.context)

        return root
    }

    private fun waitForPickUpOrder(restaurantId: Int) {
        networkService.findOrder(restaurantId).enqueue(object : Callback<JsonObject>{
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
                            val orderStatus = orderResponse.getJSONObject(i).getString("orderStatus")
                            val totalPrice = orderResponse.getJSONObject(i).getInt("totalPrice")
                            val orderDelivery = orderResponse.getJSONObject(i).getJSONObject("orderDelivery")
                            val customerAddress = orderDelivery.getString("address")
                            val orderId = orderResponse.getJSONObject(i).getInt("orderId")
                            val paymentType = orderResponse.getJSONObject(i).getString("paymentType")
                            val paymentStatus = orderResponse.getJSONObject(i).getString("paymentStatus")

                            val customerInfo = orderResponse.getJSONObject(i).getJSONObject("orderCustomer")
                            val customerPhone = customerInfo.getString("phoneNumber")
                            Log.e("오더 스테이트",orderStatus)
                            when(orderStatus) {
                                "RECEPTION" -> {
                                    lists.add(PickUpWaitingList(customerAddress,customerPhone,totalPrice,
                                    paymentType,paymentStatus))
                                }
                            }

                        }
                        pickUpRecycler.notifyDataSetChanged()


                    }

                    "주문 조회 실패" -> {

                    }
                }

            }
        })
    }

}