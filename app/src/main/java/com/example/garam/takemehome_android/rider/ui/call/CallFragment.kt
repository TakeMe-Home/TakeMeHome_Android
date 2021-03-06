package com.example.garam.takemehome_android.rider.ui.call

import android.Manifest
import android.app.Dialog
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Toast
import androidx.core.content.ContextCompat
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
import kotlinx.android.synthetic.main.fragment_call.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CallFragment : Fragment() {

    private val networkService: NetworkServiceRider by lazy {
        NetworkController.instance.networkServiceRider
    }

    private var lists = arrayListOf<CallList>()
    private lateinit var dialog : Dialog
    private lateinit var callRecycler : CallViewAdapter
    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var root : View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
        root = inflater.inflate(R.layout.fragment_call, container, false)
        val recycler = root.findViewById<RecyclerView>(R.id.callRecycler)
        callLookUp()

        var riderId = arguments?.getInt("id")

        when(riderId){
            0 -> {
                riderId = sharedViewModel.getRiderId()
            }
        }

        sharedViewModel.setRiderId(riderId!!)

        dialog = Dialog(root.context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.confirm_dialog)

        callRecycler = CallViewAdapter(lists, root.context) { callList ->
            showDialog(callList,riderId)
        }

        recycler.adapter = callRecycler
        recycler.layoutManager = LinearLayoutManager(root.context)
        recycler.setHasFixedSize(true)

        root.distanceSwitch.setOnCheckedChangeListener { compoundButton, b ->
            when {
                requireActivity().let {
                    ContextCompat.checkSelfPermission(
                        it,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    )
                } != PackageManager.PERMISSION_GRANTED ||
                        requireActivity().let {
                            ContextCompat.checkSelfPermission(
                                it,
                                Manifest.permission.ACCESS_COARSE_LOCATION
                            )
                        } != PackageManager.PERMISSION_GRANTED -> {
                    requestPermissions(
                        arrayOf(
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        ), 100
                    )
                }
                compoundButton.isChecked -> {
                    val lm: LocationManager = requireActivity()
                        .getSystemService(Context.LOCATION_SERVICE) as LocationManager
                    val location = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                    val latitude = location.latitude
                    val longitude = location.longitude

                    lists.clear()
                    callRecycler.notifyDataSetChanged()
                    nearByCall(latitude,longitude)
                }
                else -> {
                    lists.clear()
                    callRecycler.notifyDataSetChanged()
                    callLookUp()
                }
            }
        }
            return root
    }

    private fun callLookUp(){
        val errorMessage = Toast.makeText(this@CallFragment.requireContext(),"주문 조회에 실패했습니다",Toast.LENGTH_SHORT)
        networkService.callLookUp().enqueue(object : Callback<JsonObject>{
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                errorMessage.show()
            }

            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                val res = response.body()?.asJsonObject
                val data = res?.get("data")?.asJsonObject
                val orderArray = data?.get("orderFindRequestStatusResponses")?.asJsonArray
                when {
                    res?.get("message")?.asString == "주문 조회 성공" && orderArray?.size() != 0 -> {

                        for (i in 0 until orderArray?.size()!!) {
                            val customerName = orderArray.get(i).asJsonObject?.get("orderCustomer")
                                ?.asJsonObject?.get("name")?.toString()

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
                            val orderId = orderArray.get(i).asJsonObject?.get("orderId")?.asInt

                            lists.add(CallList(restaurantName.toString(),restaurantAddress.toString(),
                                deliveryAddress.toString(),deliveryPrice?.toInt()!!,0.0, orderId?.toInt()!!))
                        }
                        callRecycler.notifyDataSetChanged()
                    }
                    res?.get("message")?.asString == "주문 조회 실패" || orderArray?.size() == 0-> {
                        errorMessage.show()
                    }

                }
            }
        })
    }

    private fun nearByCall(x: Double, y: Double) {
        val errorMessage = Toast.makeText(this@CallFragment.requireContext(),"주문 조회에 실패했습니다",Toast.LENGTH_SHORT)
        networkService.nearBy(x,y).enqueue(object : Callback<JsonObject>{
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                errorMessage.show()
            }

            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                val res = response.body()?.asJsonObject
                val message = res?.get("message")?.asString
                val orderArray = res?.get("data")?.asJsonArray
                when{
                    message == "주문 조회 성공" && orderArray?.size() != 0-> {
                        for (i in 0 until orderArray?.size()!!) {
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
                            val orderId = orderArray.get(i).asJsonObject?.get("orderId")?.asInt

                            lists.add(CallList(restaurantName.toString(),restaurantAddress.toString(),
                                deliveryAddress.toString(), deliveryPrice!!,0.0, orderId!!
                            ))
                        }
                        callRecycler.notifyDataSetChanged()
                    }

                    message == "주문 조회 실패" || orderArray?.size() == 0 -> {
                        errorMessage.show()
                    }

                }
            }
        })
    }

    private fun showDialog(callList: CallList, riderId:Int) {
        dialog.show()
        dialog.setCanceledOnTouchOutside(false)
        dialog.testNameConfirm.text = callList.storeName

        dialog.testConfirmbutton.setOnClickListener {
            dialog.dismiss()
            orderAssign(callList,riderId)
        }

        dialog.testCancelbutton.setOnClickListener {
            dialog.dismiss()
        }
    }

    private fun orderAssign(callList: CallList, riderId: Int){
        val errorMessage = Toast.makeText(this@CallFragment.requireContext(),"배차에 실패했습니다",Toast.LENGTH_SHORT)
        networkService.orderAssigned(callList.orderId ,riderId).enqueue(object : Callback<JsonObject>{
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                errorMessage.show()
            }

            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                val res = response.body()

                when(res?.get("message")?.asString){
                    "주문 배차 완료" -> {
                        lists.remove(callList)
                        callRecycler.notifyDataSetChanged()
                    }
                    "주문 배차 실패" -> {
                        errorMessage.show()
                    }
                }

            }
        })
    }

}