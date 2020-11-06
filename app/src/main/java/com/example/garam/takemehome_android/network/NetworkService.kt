package com.example.garam.takemehome_android.network

import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.http.*

interface NetworkService {

    @GET("/v2/local/search/address.json")
    fun address(
        @Header("Authorization") key: String,
        @Query("query") query: String
    ): Call<JsonObject>

    @POST("/api/v1/owners/with/restaurant")
    fun signUpRestaurant(
        @Body storeInfo : JsonObject
    ):Call<JsonObject>

    @POST("/api/v1/riders")
    fun signUpRider(
        @Body riderInfo : JsonObject
    ): Call<JsonObject>

    @POST("/api/v1/customers")
    fun signUpCustomer(
        @Body customerInfo : JsonObject
    ): Call<JsonObject>

    @POST("/api/v1/riders/login")
    fun loginRider(
        @Body loginRequest : JsonObject
    ): Call<JsonObject>

    @POST("/api/v1/owners/login")
    fun loginOwner(
        @Body loginRequest: JsonObject
    ): Call<JsonObject>

    @POST("/api/v1/customers/login")
    fun loginCustomer(
        @Body loginRequest: JsonObject
    ): Call<JsonObject>

    @GET("/api/v1/orders/status/request")
    fun callLockUp() : Call<JsonObject>



}