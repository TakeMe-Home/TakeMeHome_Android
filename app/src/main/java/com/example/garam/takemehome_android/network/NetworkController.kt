package com.example.garam.takemehome_android.network

import android.app.Application
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class NetworkController : Application(){

    val baseURL = "https://13f63ff2e3cd.ngrok.io"

    lateinit var networkService: NetworkService

    companion object{
         lateinit var instance: NetworkController
 //       var instance: NetworkController = NetworkController()
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
    }


}