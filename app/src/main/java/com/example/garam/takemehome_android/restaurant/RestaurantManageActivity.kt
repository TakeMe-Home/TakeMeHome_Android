package com.example.garam.takemehome_android.restaurant

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.garam.takemehome_android.R
import com.example.garam.takemehome_android.network.NetworkController
import com.example.garam.takemehome_android.network.NetworkServiceRestaurant
import com.google.gson.JsonObject
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RestaurantManageActivity : AppCompatActivity() {

    private val networkService : NetworkServiceRestaurant by lazy{
        NetworkController.instance.networkServiceRestaurant
    }
    private lateinit var restaurantManageRecycler : RestaurantManageListViewAdapter
    private var lists = arrayListOf<RestaurantManageList>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restaurant_manage)
        val intent = intent
        val recycler = findViewById<RecyclerView>(R.id.restaurantManageRecycler)
        val ownerId = intent.getIntExtra("ownerId",0)
        Log.e("주인 id",ownerId.toString())
        restaurantLookUp(ownerId)

        restaurantManageRecycler = RestaurantManageListViewAdapter(lists,this){
                RestaurantManageList ->
            val nextIntent = Intent(this,ForRestaurantActivity::class.java)
            nextIntent.putExtra("restaurantId",RestaurantManageList.restaurantId)
            Log.e("뭐지",RestaurantManageList.restaurantId.toString())
            startActivity(nextIntent)
        }

        recycler.adapter = restaurantManageRecycler
        recycler.layoutManager = LinearLayoutManager(this)
        recycler.setHasFixedSize(true)


    }

    private fun restaurantLookUp(ownerId : Int){
        val failMessage = Toast.makeText(this@RestaurantManageActivity,"가게 조회에 실패했습니다"
            ,Toast.LENGTH_LONG)

        networkService.restaurantManage(ownerId).enqueue(object : Callback<JsonObject>{
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                failMessage.show()
            }

            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                val res = response.body()
                val message = res?.get("message")?.asString
                Log.e("데이터",res.toString())

                when{
                    message.toString() == "가게 정보 조회 성공" ->{

                        val data = res?.getAsJsonObject("data")
                        val dataObject = JSONObject(data.toString())
                        val restaurantResponse = dataObject.getJSONArray("restaurantFindAllResponse")
                        for (i in 0 until restaurantResponse.length()){
                            lists.add(RestaurantManageList(restaurantResponse.getJSONObject(i)
                                .getString("address"),restaurantResponse.getJSONObject(i).getString("name"),
                            restaurantResponse.getJSONObject(i).getInt("id"),
                            restaurantResponse.getJSONObject(i).getJSONObject("location").getDouble("x"),
                                restaurantResponse.getJSONObject(i).getJSONObject("location").getDouble("y"),
                                restaurantResponse.getJSONObject(i).getString("number")))
                            Log.e("가게 아이디",lists[i].restaurantId.toString())
                        }
                        restaurantManageRecycler.notifyDataSetChanged()

                    }

                    message.toString() == "가게 정보 조회 실패" -> {
                        failMessage.show()
                    }
                }
            }
        })
    }

}