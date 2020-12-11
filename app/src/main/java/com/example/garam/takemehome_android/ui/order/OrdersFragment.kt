package com.example.garam.takemehome_android.ui.order

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
import kotlinx.android.synthetic.main.order_info_dialog.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OrdersFragment : Fragment() {

    private val networkService : NetworkServiceRider by lazy {
        NetworkController.instance.networkServiceRider
    }

    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var orderRecycler : OrderViewAdapter
    private var lists = arrayListOf<OrderList>()
    private lateinit var dialog : Dialog

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_order, container, false)
        val recycler = root.findViewById<RecyclerView>(R.id.orderRecycler)
        dialog = Dialog(root.context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.order_info_dialog)
        val riderId = sharedViewModel.getRiderId()

        deliveryList(riderId)
        orderRecycler = OrderViewAdapter(lists,root.context){ orderList ->
            showDialog(orderList)
        }
        recycler.adapter = orderRecycler
        recycler.layoutManager = LinearLayoutManager(root.context)
        recycler.setHasFixedSize(true)

        return root
    }

    private fun deliveryList(id: Int){
        networkService.orderForRider(id).enqueue(object : Callback<JsonObject>{
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {

            }

            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                val res = response.body()?.asJsonObject
                val message = res?.get("message")?.asString
                val orderArray = res?.get("data")?.asJsonArray
                when{
                    message == "주문 조회 성공" && orderArray?.size() != 0 -> {
                        for (i in 0 until orderArray?.size()!!) {
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

                            lists.add(
                                OrderList(restaurantName.toString(),restaurantAddress.toString(),
                                deliveryAddress.toString(),deliveryPrice?.toInt()!!,0.0)
                            )
                        }
                        orderRecycler.notifyDataSetChanged()

                    }
                    message == "주문 조회 실패" || orderArray?.size() == 0-> {
                        Toast.makeText(this@OrdersFragment.requireContext(),"조회에 실패했습니다",
                            Toast.LENGTH_LONG).show()
                    }
                }
            }
        })
    }


    private fun showDialog(orderList: OrderList){
        dialog.show()
        dialog.setCanceledOnTouchOutside(false)
        dialog.orderAddress.text = "가게 주소 : ${orderList.storeAddress}"
        dialog.orderDeliveryAddressTextView.text = "배달 주소 : ${orderList.deliveryAddress}"
        dialog.orderPrice.text = "가격 : $"
        dialog.orderMethod.text = "결제 수단 : "
        dialog.orderRequest.text = "요청 사항 : "
        dialog.deliveryPriceTextView.text = "배달료 : ${orderList.deliveryPrice}원"
        dialog.order_info_confirm.setOnClickListener {
            dialog.dismiss()
        }
    }
}