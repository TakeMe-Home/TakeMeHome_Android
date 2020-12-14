package com.example.garam.takemehome_android.customer

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.garam.takemehome_android.R
import com.example.garam.takemehome_android.network.NetworkController
import com.example.garam.takemehome_android.network.NetworkService
import com.google.gson.JsonParser
import kotlinx.android.synthetic.main.activity_stand_by.*
import org.json.JSONObject


class StandByActivity : AppCompatActivity() {

    private val networkService: NetworkService by lazy {
        NetworkController.instance.networkService
    }
    private var orderState = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stand_by)

        val intent = intent
        val receptionInfo = JSONObject(intent.getStringExtra("receptionInfo"))
        val receptionInfoJson = JsonParser().parse(receptionInfo.toString()).asJsonObject


        customerHomeButton.setOnClickListener {
            when(orderState){
                "" -> {

                }
                "주문이 취소 됐어요." -> {
                    orderResultTextView.text = "결과: $orderState"
                    orderResultReason.text = "이유: "

                    finish()
                }
                "주문이 수락 됐어요." -> {
                    Toast.makeText(this,"주문이 수락 됐습니다.",Toast.LENGTH_LONG).show()

                    finish()
                }
            }
        }
    }


    override fun onStart() {
        super.onStart()
        LocalBroadcastManager.getInstance(this).registerReceiver(
            mMessageReceiver,
            IntentFilter("MyData")
        )
    }

    override fun onStop() {
        super.onStop()
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver)
    }

    private val mMessageReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent) {
            orderState = intent.getStringExtra("test")
            Log.e("???",intent.getStringExtra("test"))
        }
    }
}