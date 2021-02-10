package com.example.garam.takemehome_android.rider.ui.assignedOrder

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
import com.example.garam.takemehome_android.network.NetworkServiceRider
import com.example.garam.takemehome_android.rider.ui.SharedViewModel
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.confirm_dialog.*
import kotlinx.android.synthetic.main.delivery_info_dialog_layout.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AssignedOrderFragment : Fragment() {

    private val networkService: NetworkServiceRider by lazy {
        NetworkController.instance.networkServiceRider
    }

    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var assignedRecycler : AssignedOrderViewAdapter
    private var lists = arrayListOf<AssignedOrderList>()
    private lateinit var dialog : Dialog

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_assigned, container, false)
        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
        val recycler = root.findViewById<RecyclerView>(R.id.assignedOrderRecyclerView)
        val riderId = sharedViewModel.getRiderId()

        dialog = Dialog(root.context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.delivery_info_dialog_layout)

        assignedOrder(riderId)

        assignedRecycler = AssignedOrderViewAdapter(lists,root.context) {
            assignedOrderList ->
            showDialog(assignedOrderList)
        }

        recycler.adapter = assignedRecycler
        recycler.layoutManager = LinearLayoutManager(root.context)

        return root
    }

    private fun showDialog(assignedOrderList: AssignedOrderList) {
        dialog.show()
        dialog.setCanceledOnTouchOutside(false)

        dialog.deliveryStoreAddressTextView.text = assignedOrderList.storeAddress
        dialog.deliveryAddressTextView.text = assignedOrderList.deliveryAddress
        dialog.deliveryOrderPriceTextView.text = assignedOrderList.totalPrice.toString() +"원"
        dialog.deliveryPaymentTextView.text = ""
        dialog.deliveryRequestsTextView.text = ""
        dialog.deliveryFeeTextView.text = assignedOrderList.deliveryPrice.toString() + "원"


        when(assignedOrderList.deliveryStatus) {
            "COMPLETE" ->
            {
                dialog.deliveryCurrentStatusTextView.text = "배달 완료"
                dialog.deliveryCompleteButton.isEnabled = false
            }
            "ASSIGNED" -> {
                dialog.deliveryCurrentStatusTextView.text = "배차"
                dialog.deliveryCompleteButton.isEnabled = false
            }
            "PICK_UP" -> {
                dialog.deliveryCurrentStatusTextView.text = "픽업"
                dialog.deliveryCompleteButton.isEnabled = true
                dialog.deliveryCompleteButton.setOnClickListener {
                    deliveryComplete(assignedOrderList.orderId)
                    dialog.deliveryCompleteButton.isEnabled = false
                }
            }

        }

        dialog.deliveryInfoConfirmButton.setOnClickListener {
            dialog.dismiss()
        }
    }

    private fun assignedOrder(riderId: Int){
        val errorMessage = Toast.makeText(this@AssignedOrderFragment.requireContext(),"주문 조회에 실패했습니다",Toast.LENGTH_SHORT)

        networkService.orderListForRider(riderId).enqueue(object : Callback<JsonObject>{
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                errorMessage.show()
            }

            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                val res = response.body()?.asJsonObject
                val message = res?.get("message")?.asString
                val orderArray = res?.get("data")?.asJsonArray
                when{
                    message == "주문 조회 성공" && orderArray?.size() != 0 -> {
                        for (i in 0 until orderArray?.size()!!) {
                            val orderId = orderArray.get(i).asJsonObject?.get("orderId")?.asInt
                            val orderStatus = orderArray.get(i).asJsonObject?.get("orderStatus")?.asString
                            val customerName = orderArray.get(i).asJsonObject?.get("orderCustomer")
                                ?.asJsonObject?.get("name")?.asString
                            val customerPhoneNumber  = orderArray.get(i).asJsonObject?.get("orderCustomer")
                                ?.asJsonObject?.get("phoneNumber")?.asString
                            val restaurantName = orderArray.get(i).asJsonObject?.get("orderRestaurant")
                                ?.asJsonObject?.get("name")?.asString
                            val restaurantNumber = orderArray.get(i).asJsonObject?.get("orderRestaurant")
                                ?.asJsonObject?.get("number")?.asString
                            val restaurantAddress = orderArray.get(i).asJsonObject?.get("orderRestaurant")
                                ?.asJsonObject?.get("address")?.asString
                            val deliveryAddress = orderArray.get(i).asJsonObject?.get("orderDelivery")
                                ?.asJsonObject?.get("address")?.asString
                            val deliveryPrice = orderArray.get(i).asJsonObject?.get("orderDelivery")
                                ?.asJsonObject?.get("price")?.asInt
                            val deliveryStatus = orderArray.get(i).asJsonObject?.get("orderDelivery")
                                ?.asJsonObject?.get("status")?.asString
                            val distance = orderArray.get(i).asJsonObject?.get("orderDelivery")
                                ?.asJsonObject?.get("distance")?.asDouble
                            val totalPrice = orderArray.get(i).asJsonObject?.get("totalPrice")?.asInt

                            lists.add(
                                AssignedOrderList(restaurantName.toString(),restaurantAddress.toString(),
                                    deliveryAddress.toString(), deliveryPrice!!,distance!!, orderId!!
                                    ,orderStatus.toString(),deliveryStatus.toString(), totalPrice!!)
                            )
                        }
                        assignedRecycler.notifyDataSetChanged()

                    }
                    message == "주문 조회 실패" || orderArray?.size() == 0-> {
                        errorMessage.show()
                    }
                }
            }

        })
    }

    private fun deliveryComplete(orderId: Int) {
        val errorMessage = Toast.makeText(this@AssignedOrderFragment.requireContext(),"배달 완료에 실패했습니다",Toast.LENGTH_SHORT)
        networkService.orderComplete(orderId).enqueue(object : Callback<JsonObject>{
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                errorMessage.show()
            }

            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                val res = response.body()
                when(res?.get("message")?.asString){
                    "주문 완료 성공" -> {

                    }
                    "주문 완료 실패" -> {
                        errorMessage.show()
                    }
                }

            }
        })
    }

}