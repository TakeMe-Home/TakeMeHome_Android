package com.example.garam.takemehome_android.customer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.garam.takemehome_android.R
import com.example.garam.takemehome_android.network.NetworkController
import com.example.garam.takemehome_android.network.NetworkService
import com.google.gson.JsonObject
import org.json.JSONObject
import retrofit2.*

class ForCustomerActivity : AppCompatActivity() {

    private lateinit var viewModel : MenuSharedViewModel

    private val networkService: NetworkService by lazy {
        NetworkController.instance.networkService
    }

    private lateinit var restaurantRecycler: RestaurantListViewAdapter
    private var lists = arrayListOf<RestaurantList>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_for_customer)
        val recycler = findViewById<RecyclerView>(R.id.restaurantRecyclerView)
        viewModel = ViewModelProvider(this).get(MenuSharedViewModel::class.java)
        lookUp()
        val nextIntent = Intent(this,MenuListActivity::class.java)

        restaurantRecycler = RestaurantListViewAdapter(lists,this){
            RestaurantList->
            nextIntent.putExtra("restaurantId",RestaurantList.restaurantId)
            Log.e("고객 액티비티",RestaurantList.restaurantId.toString())
            startActivity(nextIntent)

        }

        recycler.adapter = restaurantRecycler

        recycler.layoutManager = LinearLayoutManager(this)
        recycler.setHasFixedSize(true)

    }

    private fun lookUp(){
        networkService.restaurantLookUp().enqueue(object : Callback<JsonObject>{
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {

            }

            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                val res = response.body()
                Log.e("리스폰스",res.toString())
                val message = res?.get("message")?.asString
                val data = res?.get("data")?.asJsonObject
                val dataObject = JSONObject(data.toString())
                val restaurantResponse = dataObject.getJSONArray("restaurantFindAllResponse")
                when {
                    message == "가게 정보 조회 성공" ->{
                        for (i in 0 .. data!!.size()){
                            lists.add(RestaurantList(restaurantResponse.getJSONObject(i).getString("name"),
                                restaurantResponse.getJSONObject(i).getInt("id")))
                            Log.e("가게 정보",restaurantResponse.getJSONObject(i).getString("name"))
                            viewModel.setRestaurantId(restaurantResponse.getJSONObject(i).getInt("id"))
                        }
                        restaurantRecycler.notifyDataSetChanged()
                    }
                }
            }
        })
    }
}