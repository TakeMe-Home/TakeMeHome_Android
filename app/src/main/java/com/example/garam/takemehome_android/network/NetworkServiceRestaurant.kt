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

    @GET("/api/v1/restaurants/{ownerId}")
    fun restaurantManage(
        @Path ("ownerId") ownerId : Int
    ): Call<JsonObject>

    @POST("/api/v1/orders/refuse")
    fun refuseOrder(
        @Body refuseRequest : JsonObject
    ): Call<JsonObject>

    @GET("/api/v1/menus/{restaurantId}")
    fun menuLookUp(
        @Path("restaurantId") id : Int
    ): Call<JsonObject>

    @POST("/api/v1/restaurants")
    fun restaurantAdd(
        @Body saveRequest : JsonObject
    ): Call<JsonObject>

    @PUT("/api/v1/menus/menu/{id}")
    fun menuUpdate(
        @Path("id") id : Int,
        @Body updateRequest : JsonObject
    ): Call<JsonObject>

    @GET("/api/v1/orders/{restaurantId}")
    fun findOrder(
        @Path("restaurantId") id : Int
    ): Call<JsonObject>

    @DELETE("/api/v1/menus/menu/{id}")
    fun menuDelete(
        @Path("id") menuId: Int
    ): Call<JsonObject>

    @GET("/api/v1/orders/status/request/restaurant")
    fun findWaitForOrder(
        @Query ("address") address : String,
        @Query ("name") name : String
    ): Call<JsonObject>

}