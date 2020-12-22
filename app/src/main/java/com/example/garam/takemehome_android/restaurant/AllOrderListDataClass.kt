package com.example.garam.takemehome_android.restaurant

data class AllOrderListDataClass(
    val customerAddress: String,
    val customerPhone : String,
    val totalPrice : Int,
    val paymentType : String,
    val paymentStatus : String,
    val orderId : Int
)
