package com.example.garam.takemehome_android.customer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.garam.takemehome_android.R
import com.example.garam.takemehome_android.network.NetworkController
import com.example.garam.takemehome_android.network.NetworkService
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import kotlinx.android.synthetic.main.activity_pay_ment.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PaymentActivity : AppCompatActivity() {
    private val networkService: NetworkService by lazy {
        NetworkController.instance.networkService
    }
    private lateinit var viewModel : MenuSharedViewModel
    private var lastTotalPrice = 0
    private var orderPrice = 0
    private var receptionInfo = JSONObject()

    private var orderInfo = JSONObject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pay_ment)

        viewModel = ViewModelProvider(this).get(MenuSharedViewModel::class.java)
        val intent = intent
        orderPrice = intent.getIntExtra("lastPrice",0)
        val restaurantId = intent.getIntExtra("restaurantId",0)
        val customerId = intent.getIntExtra("customerId",0)
        val restaurantName = intent.getStringExtra("restaurantName")
        receptionInfo = JSONObject(intent.getStringExtra("json"))

        orderInfo = JSONObject(intent.getStringExtra("orderInfo"))
        Log.e("받을때 Json",receptionInfo.toString())

        deliveryPriceInquiry(restaurantId,customerId)

        paymentRestaurantNameTextView.text = restaurantName
        paymentOrderPriceTextView.text = orderPrice.toString() + "원"

        radioGroup.setOnCheckedChangeListener { radioGroup, i ->
            when (i) {
                R.id.unTactCard -> {
                    receptionInfo.put("paymentStatus","COMPLITE")
                    receptionInfo.put("paymentType","CARD")
                }
                R.id.contactCash -> {
                    receptionInfo.put("paymentStatus","NONE")
                    receptionInfo.put("paymentType","CASH")
                }
                R.id.contactCard -> {
                    receptionInfo.put("paymentStatus","NONE")
                    receptionInfo.put("paymentType","CARD")
                }
            }
        }

        lastPaymentButton.setOnClickListener {

            val nextIntent = Intent(this,StandByActivity::class.java)
            nextIntent.putExtra("orderInfo",orderInfo.toString())
            startActivityForResult(nextIntent,100)

            val lastReceptionInfo = JsonParser().parse(receptionInfo.toString()).asJsonObject
            Log.e("최종 결제 정보",lastReceptionInfo.toString())
        //    orderReception(lastReceptionInfo)
        }

        paymentCancelButton.setOnClickListener {
            finish()
            Toast.makeText(this,"결제를 취소했습니다.",Toast.LENGTH_LONG).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when {
            requestCode == 100 -> {

            }
        }
    }

    private fun deliveryPriceInquiry(restaurantId: Int,customerId: Int){
        networkService.deliveryPrice(restaurantId, customerId).enqueue(object : Callback<JsonObject>{
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {

            }

            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                val message = response.body()?.get("message")
                var deliveryPrice = 0
                Log.e("ㄻㅇㄹ",response.body().toString())
                Log.e("fadfadsf",message.toString())
                when {
                    response.body()?.get("statusCode")?.asInt == 200 -> {
                        deliveryPrice = response.body()
                            ?.getAsJsonObject("data")?.get("price")?.asInt!!
                        paymentDeliveryPriceTextView.text = deliveryPrice.toString() + "원"
                        lastPrice.text = (orderPrice + deliveryPrice.toInt()).toString() + "원"
                        receptionInfo.put("totalPrice",(orderPrice + deliveryPrice.toInt())
                            .toString().toInt())

                    }
                    message.toString() == "배달 가격 조회 실패" -> {
                        Toast.makeText(this@PaymentActivity,"배달 가격 조회에 실패하였습니다",
                        Toast.LENGTH_LONG).show()
                    }
                }
            }
        })
    }



    private fun orderReception(receptionInfo: JsonObject){
        networkService.reception(receptionInfo).enqueue(object : Callback<JsonObject> {
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {

            }

            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                when{
                    response.isSuccessful -> {
                        Toast.makeText(this@PaymentActivity, "주문에 성공하였습니다",
                            Toast.LENGTH_LONG).show()
                        finish()
                    }
                }
            }
        })
    }
}