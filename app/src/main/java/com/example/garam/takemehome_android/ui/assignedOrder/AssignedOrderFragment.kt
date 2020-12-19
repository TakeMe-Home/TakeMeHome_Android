package com.example.garam.takemehome_android.ui.assignedOrder

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
import com.example.garam.takemehome_android.ui.SharedViewModel
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

        when(assignedOrderList.deliveryStatus) {
            "COMPLETE" ->
            {
                dialog.deliveryCompleteButton.isEnabled = false
            }
            "ASSIGNED" -> {
                dialog.deliveryCompleteButton.isEnabled = false
            }
            "PICK_UP" -> {
                dialog.deliveryCompleteButton.isEnabled = true
                dialog.deliveryCompleteButton.setOnClickListener {
                    deliveryComplete(assignedOrderList.orderId)
                }
            }

        }

        dialog.deliveryInfoConfirmButton.setOnClickListener {
            dialog.dismiss()
        }
    }

    private fun assignedOrder(riderId: Int){
        networkService.orderListForRider(riderId).enqueue(object : Callback<JsonObject>{
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {

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
                                ?.asJsonObject?.get("name")?.toString()
                            val customerPhoneNumber  = orderArray.get(i).asJsonObject?.get("orderCustomer")
                                ?.asJsonObject?.get("phoneNumber")?.toString()
                            val restaurantName = orderArray.get(i).asJsonObject?.get("orderRestaurant")
                                ?.asJsonObject?.get("name")?.toString()
                            val restaurantNumber = orderArray.get(i).asJsonObject?.get("orderRestaurant")
                                ?.asJsonObject?.get("number")?.toString()
                            val restaurantAddress = orderArray.get(i).asJsonObject?.get("orderRestaurant")
                                ?.asJsonObject?.get("address")?.toString()
                            val deliveryAddress = orderArray.get(i).asJsonObject?.get("orderDelivery")
                                ?.asJsonObject?.get("address")?.toString()
                            val deliveryPrice = orderArray.get(i).asJsonObject?.get("orderDelivery")
                                ?.asJsonObject?.get("price")?.asInt
                            val deliveryStatus = orderArray.get(i).asJsonObject?.get("orderDelivery")
                                ?.asJsonObject?.get("status")?.asString

                            lists.add(
                                AssignedOrderList(restaurantName.toString(),restaurantAddress.toString(),
                                    deliveryAddress.toString(),deliveryPrice?.toInt()!!,0.0,orderId?.toInt()!!
                                    ,orderStatus.toString(),deliveryStatus.toString())
                            )
                        }
                        assignedRecycler.notifyDataSetChanged()

                    }
                    message == "주문 조회 실패" || orderArray?.size() == 0-> {
                        Toast.makeText(this@AssignedOrderFragment.requireContext(),"조회에 실패했습니다",
                            Toast.LENGTH_LONG).show()
                    }
                }
            }

        })
    }

    private fun deliveryComplete(orderId: Int) {
        networkService.orderComplete(orderId).enqueue(object : Callback<JsonObject>{
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {

            }

            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                dialog.deliveryCompleteButton.isEnabled = false
            }
        })
    }

}