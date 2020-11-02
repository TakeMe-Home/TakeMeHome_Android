package com.example.garam.takemehome_android.network

import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.*

interface NetworkService {

    @GET("/v2/local/search/address.json")
    fun address(
        @Header("Authorization") key: String,
        @Query("query") query: String
    ): Call<JsonObject>

    @POST("")
    fun signUpStore(
        @Body storeInfo : JsonObject
    ):Call<JsonObject>

    @POST("")
    fun signUpRider(
        @Body riderInfo : JsonObject
    ): Call<JsonObject>

    @POST("")
    fun signUpCustomer(
        @Body customerInfo : JsonObject
    ): Call<JsonObject>

}