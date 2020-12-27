package com.example.garam.takemehome_android.restaurant.pickup

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
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
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import kotlinx.android.synthetic.main.pick_up_request_dialog_layout.*
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
    private val lists = arrayListOf<AllOrderListDataClass>()
    private lateinit var pickUpRecycler : PickUpWaitListViewAdapter
    private lateinit var dialog : Dialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        root = inflater.inflate(R.layout.fragment_pick_up, container, false)

        val recycler  = root.findViewById<RecyclerView>(R.id.pickUpRecyclerVIew)

        sharedViewModel = ViewModelProvider(requireActivity()).get(RestaurantSharedViewModel::class.java)

        dialog = Dialog(root.context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.pick_up_request_dialog_layout)

        val restaurantId = sharedViewModel.getId()
        sharedViewModel.setId(restaurantId!!)

        waitForPickUpOrder(restaurantId)

        pickUpRecycler = PickUpWaitListViewAdapter(lists,root.context){
                allOrderListDataClass ->
            requestDeliveryDialog(allOrderListDataClass)
        }

        recycler.adapter = pickUpRecycler
        recycler.layoutManager = LinearLayoutManager(root.context)

        return root
    }

    private fun waitForPickUpOrder(restaurantId: Int) {
        val errorMessage = Toast.makeText(root.context,"주문 조회에 실패했습니다",Toast.LENGTH_SHORT)
        networkService.findOrder(restaurantId).enqueue(object : Callback<JsonObject>{
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

                        for (i in 0 until orderResponse.length()){
                            val orderStatus = orderResponse.getJSONObject(i).getString("orderStatus")
                            val totalPrice = orderResponse.getJSONObject(i).getInt("totalPrice")
                            val orderDelivery = orderResponse.getJSONObject(i).getJSONObject("orderDelivery")
                            val customerAddress = orderDelivery.getString("address")
                            val deliveryStatus = orderDelivery.getString("status")
                            val orderId = orderResponse.getJSONObject(i).getInt("orderId")
                            val paymentType = orderResponse.getJSONObject(i).getString("paymentType")
                            val paymentStatus = orderResponse.getJSONObject(i).getString("paymentStatus")

                            val customerInfo = orderResponse.getJSONObject(i).getJSONObject("orderCustomer")
                            val customerPhone = customerInfo.getString("phoneNumber")

                            when(deliveryStatus) {
                                "NONE" -> {
                                    lists.add(AllOrderListDataClass(customerAddress,customerPhone,totalPrice,
                                    paymentType,paymentStatus,orderId,deliveryStatus))
                                }
                            }
                        }
                        pickUpRecycler.notifyDataSetChanged()
                    }

                    "주문 조회 실패" -> {
                        errorMessage.show()
                    }
                }
            }
        })
    }

    private fun requestDeliveryDialog(pickUpWaitingList: AllOrderListDataClass){
        dialog.show()
        dialog.setCanceledOnTouchOutside(false)

        dialog.pickupRequestButton.setOnClickListener {
            val deliveryInfo = JSONObject()
            val cookingTime = dialog.cookingTimeEditText.text.toString()

            deliveryInfo.put("cookingTime",cookingTime.toInt())
            val deliveryInfoJson = JsonParser().parse(deliveryInfo.toString()).asJsonObject

            requestDeliveryMethod(pickUpWaitingList.orderId,deliveryInfoJson)
        }
        dialog.pickUpRequestCancelButton.setOnClickListener {
            dialog.dismiss()
        }
    }


    private fun requestDeliveryMethod(orderId: Int, deliveryInfo: JsonObject){
        val errorMessage = Toast.makeText(root.context,"배달 요청에 실패했습니다",Toast.LENGTH_SHORT)

        networkService.requestDelivery(orderId,deliveryInfo).enqueue(object : Callback<JsonObject>{
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                errorMessage.show()
            }

            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                val res = response.body()
                when(res?.get("message")?.asString){
                    "주문 배달 요청 성공" -> {
                        dialog.dismiss()
                        Toast.makeText(root.context,"배달 요청에 성공하였습니다",Toast.LENGTH_SHORT).show()
                    }

                    "주문 배달 요청 실패" -> {
                        errorMessage.show()
                    }
                }
            }
        })
    }
}