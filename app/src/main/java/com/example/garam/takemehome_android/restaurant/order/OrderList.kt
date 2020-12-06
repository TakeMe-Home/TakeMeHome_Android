package com.example.garam.takemehome_android.restaurant.order

import com.example.garam.takemehome_android.restaurant.receipt.ReceiptMenuList

data class OrderList(
    val deliveryAddress : String,
    val customerPhone : String,
    val totalPrice : Int,
    val menuCounts : ArrayList<ReceiptMenuList>,
    val riderStatus : String
)