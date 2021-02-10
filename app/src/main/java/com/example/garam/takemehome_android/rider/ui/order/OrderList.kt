package com.example.garam.takemehome_android.rider.ui.order

data class OrderList  (
    val storeName: String,
    val storeAddress: String,
    val deliveryAddress: String,
    val deliveryPrice: Int,
    val nearByDistance: Double,
    val orderId : Int,
    val orderStatus : String
)