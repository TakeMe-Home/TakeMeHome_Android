package com.example.garam.takemehome_android.network

import android.app.Application
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import java.util.concurrent.TimeUnit

class NetworkController : Application(){

    val baseURL = "http://b8a707fec94f.ngrok.io"

    lateinit var networkService: NetworkService
    lateinit var networkServiceRider: NetworkService_Rider
    lateinit var networkServiceRestaurant : NetworkService_Restaurant

    companion object{
         lateinit var instance: NetworkController
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        buildNetwork()
    }

    fun buildNetwork(){
        val retrofit: Retrofit = Retrofit.Builder().baseUrl(baseURL).addConverterFactory(GsonConverterFactory.create())
            .client(OkHttpClient.Builder().connectTimeout(30,TimeUnit.SECONDS).readTimeout(30,TimeUnit.SECONDS)
                .writeTimeout(30,TimeUnit.SECONDS).addInterceptor(HttpLoggingInterceptor()).build()).build()

        networkService = retrofit.create(NetworkService::class.java)
        networkServiceRider = retrofit.create(NetworkService_Rider::class.java)
        networkServiceRestaurant = retrofit.create(NetworkService_Restaurant::class.java)
    }


}