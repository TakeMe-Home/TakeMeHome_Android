package com.example.garam.takemehome_android.network

import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface NetworkServiceRider {

    @POST("/api/v1/riders")
    fun signUpRider(
        @Body riderInfo : JsonObject
    ): Call<JsonObject>

    @POST("/api/v1/riders/login")
    fun loginRider(
        @Body loginRequest : JsonObject
    ): Call<JsonObject>

    @GET("/api/v1/orders/status/request")
    fun callLookUp() : Call<JsonObject>

    @POST("/api/v1/orders/nearby")
    fun nearBy(
        @Body locationInfo : JsonObject
    ): Call<JsonObject>
}