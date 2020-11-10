package com.example.garam.takemehome_android.customer

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
import retrofit2.*

class MenuListActivity : AppCompatActivity() {
    private lateinit var viewModel : MenuSharedViewModel
    private var menuLists = arrayListOf<MenuList>()
    private val networkService: NetworkService by lazy {
        NetworkController.instance.networkService
    }

    private lateinit var menuRecycler:MenuListViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu_list)
        val recycler = findViewById<RecyclerView>(R.id.menuRecycler)
        val intent = intent
        val restaurantId = intent.getIntExtra("restaurantId",0)
        Log.e("레스토랑 아이디",restaurantId.toString())
        viewModel = ViewModelProvider(this).get(MenuSharedViewModel::class.java)
        menuLookUp(restaurantId)

        menuRecycler = MenuListViewAdapter(menuLists,this){
            menuList ->
        }

        recycler.adapter = menuRecycler
        recycler.layoutManager = LinearLayoutManager(this)
        recycler.setHasFixedSize(true)
    }

    private fun menuLookUp(id :Int){
        networkService.menuLookUp(id).enqueue(object : Callback<JsonObject> {
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {

            }

            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                val res = response.body()
                Log.e("리스폰스",res.toString())
            }
        })
    }



}