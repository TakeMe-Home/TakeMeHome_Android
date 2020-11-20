package com.example.garam.takemehome_android.customer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.garam.takemehome_android.R
import com.example.garam.takemehome_android.firebase.FirebaseMessagingServices
import com.example.garam.takemehome_android.network.NetworkController
import com.example.garam.takemehome_android.network.NetworkService
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StandByActivity : AppCompatActivity() {

    private val networkService: NetworkService by lazy {
        NetworkController.instance.networkService
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stand_by)

        val intent = intent
        val orderInfoJson = JSONObject(intent.getStringExtra("orderInfo"))
        val orderInfo = JsonParser().parse(orderInfoJson.toString()).asJsonObject

        order(orderInfo)
    }

    private fun order(orderInfo: JsonObject){
        networkService.orderRequest(orderInfo).enqueue(object : Callback<JsonObject>{
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {

            }

            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {

            }
        })

    }
}