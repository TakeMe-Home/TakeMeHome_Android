package com.example.garam.takemehome_android.restaurant.pickup

data class PickUpWaitingList (
    val customerAddress: String,
    val customerPhone : String,
    val totalPrice : Int,
    val paymentType : String,
    val paymentStatus : String
)