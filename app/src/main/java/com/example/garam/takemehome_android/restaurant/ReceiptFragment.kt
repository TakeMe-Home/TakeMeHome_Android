package com.example.garam.takemehome_android.restaurant

import android.app.Dialog
import android.os.Bundle
import android.util.Log
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
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import kotlinx.android.synthetic.main.receipt_cancel_dialog_layout.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ReceiptFragment : Fragment() {

    private val networkService: NetworkServiceRestaurant by lazy {
        NetworkController.instance.networkServiceRestaurant
    }

    private lateinit var dialog : Dialog
    private lateinit var receiptRecycler : ReceiptViewAdapter
    private val lists = arrayListOf<ReceiptList>()
    private val menuList = arrayListOf<ReceiptMenuList>()
    private var refuseJson = JSONObject()
    private lateinit var root : View
    private lateinit var sharedViewModel : RestaurantSharedViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        root = inflater.inflate(R.layout.fragment_receipt, container, false)

        val recycler = root.findViewById<RecyclerView>(R.id.receiptRecycler)

        dialog = Dialog(root.context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.receipt_cancel_dialog_layout)

        sharedViewModel = ViewModelProvider(requireActivity()).get(RestaurantSharedViewModel::class.java)

        var restaurantId = arguments?.getInt("id")

        when (restaurantId) {
            0 -> {
                restaurantId = sharedViewModel.getId()
            }
        }
        sharedViewModel.setId(restaurantId!!)
        Log.e("주문 ",restaurantId.toString())
    //    findAllOrder(restaurantId)

        receiptRecycler = ReceiptViewAdapter(lists,root.context){
            ReceiptList ->{
        }
            refuseDialog(1,ReceiptList)
        }

        recycler.adapter = receiptRecycler
        recycler.layoutManager = LinearLayoutManager(root.context)
        recycler.setHasFixedSize(true)

        receiptRecycler.notifyDataSetChanged()

        return root
    }

    private fun refuseDialog(customerId: Int,receiptList: ReceiptList){

        dialog.show()
        dialog.setCanceledOnTouchOutside(false)
        var refuseReason = ""
        refuseJson.put("customerId",customerId)

        dialog.receiptRadioGroup.setOnCheckedChangeListener { radioGroup, i ->

            when(i){
                R.id.cancelReason1 -> {
                    refuseReason = "재료부족"
                    refuseJson.put("reason",refuseReason)
                }
                R.id.cancelReason2 -> {
                    refuseReason = "마감"
                    refuseJson.put("reason",refuseReason)
                }
                R.id.cancelReason3 -> {
                    refuseReason = "거리가 멀음"
                    refuseJson.put("reason",refuseReason)
                }
            }
        }
        dialog.receiptRefuseButton.setOnClickListener {

            when(refuseReason){
                "" -> {
                    Toast.makeText(root.context,"취소 사유를 선택해주세요",Toast.LENGTH_LONG).show()
                }
                else -> {

                    val refuseInfoJson = JsonParser().parse(refuseJson.toString()).asJsonObject
                    receiptRefuse(refuseInfoJson)
                    dialog.dismiss()
                    lists.remove(receiptList)
                    receiptRecycler.notifyDataSetChanged()
                }
            }
        }

        dialog.receiptAcceptButton.setOnClickListener{
            dialog.dismiss()
        }
    }

    private fun receiptAccept(acceptInfo: JsonObject){

    }


    private fun receiptRefuse(refuseInfo: JsonObject){
        networkService.refuseOrder(refuseInfo).enqueue(object: Callback<JsonObject>{
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {

            }

            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {

            }
        })
    }

}