package com.example.garam.takemehome_android.network

import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.*

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

    @GET("/api/v1/orders/nearby")
    fun nearBy(
        @Query("x") x : Double,
        @Query("y") y : Double
    ): Call<JsonObject>

    @GET("/api/v1/orders/riders/{riderId}")
    fun orderListForRider(
        @Path("riderId") id : Int
    ): Call<JsonObject>

    @GET("/api/v1/orders/riders/{riderId}/assigned")
    fun orderForRider(
        @Path("riderId") id : Int
    ): Call<JsonObject>

    @PUT("/api/v1/orders/order/{orderId}/assigned/{riderId}")
    fun orderAssigned(
        @Path("orderId") orderId : Int,
        @Path("riderId") riderId: Int
    ): Call<JsonObject>

    @PUT("/api/v1/orders/order/{orderId}/complete")
    fun orderComplete(
        @Path ("orderId") orderId: Int
    ): Call<JsonObject>

    @PUT("/api/v1/orders/order/{orderId}/pickup")
    fun orderPickUp(
        @Path ("orderId") orderId: Int
    ): Call<JsonObject>

}