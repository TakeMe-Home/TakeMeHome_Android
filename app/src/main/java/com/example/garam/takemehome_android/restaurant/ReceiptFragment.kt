package com.example.garam.takemehome_android.restaurant

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.garam.takemehome_android.R
import com.example.garam.takemehome_android.network.NetworkController
import com.example.garam.takemehome_android.network.NetworkServiceRestaurant
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import kotlinx.android.synthetic.main.fragment_receipt.*
import kotlinx.android.synthetic.main.fragment_receipt.view.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ReceiptFragment : Fragment() {

    private val networkService: NetworkServiceRestaurant by lazy {
        NetworkController.instance.networkServiceRestaurant
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_receipt, container, false)

        val menu = JSONObject()
        val sharedViewModel = ViewModelProvider(this).get(RestaurantSharedViewModel::class.java)
        val restaurantId = sharedViewModel.getId()
        root.menuRegisterConfirmButton.setOnClickListener {
            val name = menuRegisterName.text
            val price = menuRegisterPrice.text

            menu.put("name",name.toString())
            menu.put("price",price.toString().toInt())
            menu.put("restaurantId",restaurantId)

            val json = JsonParser().parse(menu.toString()).asJsonObject
            menuRegister(json)
        }

        return root
    }

    private fun menuRegister(menuInfo : JsonObject){
        networkService.menuRequest(menuInfo).enqueue(object : Callback<JsonObject>{
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {

            }

            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {

            }
        })
    }

}