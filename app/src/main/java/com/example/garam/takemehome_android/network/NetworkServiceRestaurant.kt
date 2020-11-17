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

    @POST("/api/v1/menus")
    fun menuRequest(
        @Body registerRequest: JsonObject
    ): Call<JsonObject>

    @PUT("/api/v1/menus/menu/{id}/sale")
    fun menuSale(
        @Path ("id") menuId : Int
    ):Call<JsonObject>

    @PUT("/api/v1/menus/menu/{id}/soldout")
    fun menuSoldOut(
        @Path ("id") menuId : Int
    ): Call<JsonObject>

}