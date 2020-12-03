package com.example.garam.takemehome_android.restaurant

data class OrderList(
    val deliveryAddress : String,
    val customerPhone : String,
    val totalPrice : Int,
    val menuCounts : ArrayList<ReceiptMenuList>,
    val riderStatus : String
)