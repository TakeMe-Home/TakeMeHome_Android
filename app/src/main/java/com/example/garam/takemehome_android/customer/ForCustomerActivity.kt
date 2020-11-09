package com.example.garam.takemehome_android.customer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.garam.takemehome_android.R
import com.example.garam.takemehome_android.network.NetworkController
import com.example.garam.takemehome_android.network.NetworkService
import retrofit2.*

class ForCustomerActivity : AppCompatActivity() {

    private val networkService: NetworkService by lazy {
        NetworkController.instance.networkService
    }

    private lateinit var restaurantRecycler: RestaurantListViewAdapter
    private var lists = arrayListOf<RestaurantList>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_for_customer)
        val recycler = findViewById<RecyclerView>(R.id.restaurantRecyclerView)

        lists.add(RestaurantList("곱창고","갈산동","0325564516"))

        restaurantRecycler = RestaurantListViewAdapter(lists,this){
            RestaurantList->

        }

        recycler.adapter = restaurantRecycler
        recycler.layoutManager = LinearLayoutManager(this)
        recycler.setHasFixedSize(true)

    }
}