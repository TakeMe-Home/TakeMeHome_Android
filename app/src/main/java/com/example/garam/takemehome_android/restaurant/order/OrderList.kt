package com.example.garam.takemehome_android.restaurant.order

import com.example.garam.takemehome_android.restaurant.receipt.ReceiptMenuList

data class OrderList(
    val customerAddress: String,
    val customerPhone : String,
    val totalPrice : Int,
    val paymentType : String,
    val paymentStatus : String,
    val orderId : Int
)
