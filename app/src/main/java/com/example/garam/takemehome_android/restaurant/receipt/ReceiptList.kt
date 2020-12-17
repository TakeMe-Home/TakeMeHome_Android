package com.example.garam.takemehome_android.restaurant.receipt

data class ReceiptList(
    val customerAddress : String,
    val totalPrice : Int,
    val menuNameCount : ArrayList<ReceiptMenuList>,
    val orderId : Int,
    val customerId : Int
)