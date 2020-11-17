package com.example.garam.takemehome_android.customer

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pay_ment)

        viewModel = ViewModelProvider(this).get(MenuSharedViewModel::class.java)
        Thread.sleep(1000)
        val receptionInfo = viewModel.getReceptionInfo()
        Log.e("받을때 Json",receptionInfo.toString())

        radioGroup.setOnCheckedChangeListener { radioGroup, i ->

            when{
                i == R.id.unTactCard -> {
                    receptionInfo?.put("paymentStatus","COMPLITE")
                    receptionInfo?.put("paymentType","CARD")
                }
                i == R.id.contactCash -> {
                    receptionInfo?.put("paymentType","CASH")
                }
                i == R.id.contactCard -> {
                    receptionInfo?.put("paymentType","CARD")
                }
            }
        }

        lastPaymentButton.setOnClickListener {
            val lastReceptionInfo = JsonParser().parse(receptionInfo.toString()).asJsonObject
            Log.e("최종 결제 정보",lastReceptionInfo.toString())
            orderReception(lastReceptionInfo)
        }

        paymentCancelButton.setOnClickListener {
            finish()
            Toast.makeText(this,"결제를 취소했습니다.",Toast.LENGTH_LONG).show()
        }
    }



    private fun orderReception(receptionInfo: JsonObject){
        networkService.reception(receptionInfo).enqueue(object : Callback<JsonObject> {
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {

            }

            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {

            }
        })
    }
}