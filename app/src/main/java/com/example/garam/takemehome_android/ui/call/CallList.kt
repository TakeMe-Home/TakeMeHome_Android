package com.example.garam.takemehome_android.ui.call

data class CallList (
    val storeName: String,
    val storeAddress: String,
    val deliveryAddress: String,
    val deliveryPrice: Int,
    val nearByDistance: Double
)