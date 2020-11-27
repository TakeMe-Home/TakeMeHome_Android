package com.example.garam.takemehome_android.restaurant

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.garam.takemehome_android.R
import com.example.garam.takemehome_android.network.KakaoApi
import com.example.garam.takemehome_android.network.NetworkController
import com.example.garam.takemehome_android.network.NetworkService
import com.example.garam.takemehome_android.network.NetworkServiceRestaurant
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import kotlinx.android.synthetic.main.activity_restaurant_manage.*
import kotlinx.android.synthetic.main.restaurant_add_dialog_layout.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RestaurantManageActivity : AppCompatActivity() {

    private val networkService : NetworkServiceRestaurant by lazy{
        NetworkController.instance.networkServiceRestaurant
    }
    private lateinit var restaurantManageRecycler : RestaurantManageListViewAdapter
    private var lists = arrayListOf<RestaurantManageList>()
    private lateinit var dialog : Dialog
    private var restaurantJson = JSONObject()
    private var locationJson = JSONObject()
    private var latitude : Double = 0.0
    private var longitude : Double= 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restaurant_manage)
        val intent = intent
        val recycler = findViewById<RecyclerView>(R.id.restaurantManageRecycler)
        val ownerId = intent.getIntExtra("ownerId",0)
        Log.e("주인 id",ownerId.toString())
        restaurantLookUp(ownerId)

        dialog = Dialog(this)
        dialog.setContentView(R.layout.restaurant_add_dialog_layout)

        restaurantAddButton.setOnClickListener {
            showAddDialog(ownerId)
        }

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

    private fun showAddDialog(ownerId: Int){

        var textAddress = ""

        dialog.show()

        dialog.additionalDetailAddress.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(p0: Editable?) {
                textAddress = p0.toString()
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

        })

        dialog.searchRoadAddressButton.setOnClickListener {
            restaurantAddress(dialog.searchRoadAddress.text.toString())
        }

        dialog.restaurantAddConfirm.setOnClickListener {

            when{
                textAddress == "" ->{
                    Toast.makeText(this,"상세 주소를 입력하세요",Toast.LENGTH_LONG).show()
                }
                dialog.additionalRestaurantName.text.toString() == "" -> {
                    Toast.makeText(this,"이름을 입력하세요",Toast.LENGTH_LONG).show()
                }
                dialog.additionalRestaurantNumber.text.toString() == "" -> {
                    Toast.makeText(this,"전화번호를 입력하세요",Toast.LENGTH_LONG).show()
                }
                else -> {
                    restaurantJson.put("address",textAddress)
                    restaurantJson.put("location",locationJson)
                    restaurantJson.put("name",dialog.additionalRestaurantName.text.toString())
                    restaurantJson.put("number",dialog.additionalRestaurantNumber.text.toString())
                    restaurantJson.put("ownerId",ownerId)

                    val restaurantInfo = JsonParser().parse(restaurantJson.toString()).asJsonObject

                    restaurantAdd(restaurantInfo)
                }
            }

        }

        dialog.restaurantAddCancel.setOnClickListener {
            dialog.dismiss()
        }

    }

    private fun restaurantAdd(restaurantAddInfo: JsonObject){
        val failMessage = Toast.makeText(this@RestaurantManageActivity,
            "가게 등록에 실패하였습니다",Toast.LENGTH_LONG)

        networkService.restaurantAdd(restaurantAddInfo).enqueue(object : Callback<JsonObject>{
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                failMessage.show()
            }

            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                val res = response.body()
                when(res?.get("message")?.asString){
                    "가게 등록 성공" -> {
                        val resId = res.get("data")?.asInt
                        lists.add(RestaurantManageList(dialog.additionalDetailAddress.text.toString(),
                        dialog.additionalRestaurantName.text.toString(), resId!!,latitude,longitude,
                        dialog.additionalRestaurantNumber.text.toString()))
                        dialog.dismiss()

                        restaurantManageRecycler.notifyDataSetChanged()
                        Toast.makeText(this@RestaurantManageActivity,"가게 등록에 성공했습니다",Toast.LENGTH_LONG
                        ).show()
                    }
                    "가게 등록 실패" -> {
                        failMessage.show()
                    }
                }
            }
        })

    }

    private fun restaurantAddress(keyword: String) {
        val failMessage = Toast.makeText(this,"주소 검색에 실패하였습니다"
            ,Toast.LENGTH_LONG)
        val retrofit: Retrofit = Retrofit.Builder().baseUrl(KakaoApi.instance.KakaoURL)
            .addConverterFactory(GsonConverterFactory.create()).build()

        val networkService = retrofit.create(NetworkService::class.java)
        val addressSearch : Call<JsonObject> = networkService.address(
            KakaoApi.instance.kakaoKey,
            keyword
        )
        addressSearch.enqueue(object : Callback<JsonObject>{
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                failMessage.show()
            }
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                val res = response.body()

                val documents = res?.getAsJsonArray("documents")
                val meta = res?.getAsJsonObject("meta")
                val totalCount = meta?.get("total_count")?.asInt
                if (totalCount == 1) {
                    val add = documents?.asJsonArray?.get(0)
                    val addInfo = add?.asJsonObject?.get("address")
                    when {
                        addInfo != null -> {
                            val addressName = JSONObject(addInfo.toString()).getString("address_name")
                            val x = JSONObject(addInfo.toString()).getDouble("x")
                            val y = JSONObject(addInfo.toString()).getDouble("y")
                            latitude = y
                            longitude = x
                            locationJson.put("x",latitude)
                            locationJson.put("y",longitude)
                            dialog.additionalDetailAddress.setText(addressName)

                        }
                        else -> {
                            failMessage.show()
                        }
                    }
                } else {
                    failMessage.show()
                }
            }
        })
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