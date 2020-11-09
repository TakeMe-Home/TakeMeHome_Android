package com.example.garam.takemehome_android.network

import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.*

interface NetworkServiceRestaurant {

    @POST("/api/v1/owners/with/restaurant")
    fun signUpRestaurant(
        @Body storeInfo : JsonObject
    ):Call<JsonObject>

    @POST("/api/v1/owners/login")
    fun loginOwner(
        @Body loginRequest: JsonObject
    ): Call<JsonObject>

}