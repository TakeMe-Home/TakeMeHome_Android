package com.example.garam.takemehome_android.restaurant

data class ReceiptList(
    val customerAddress : String,
    val totalPrice : Int,
    val menuNameCount : ArrayList<ReceiptMenuList>
)