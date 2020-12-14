package com.example.garam.takemehome_android.restaurant

import androidx.lifecycle.ViewModel

class RestaurantSharedViewModel : ViewModel(){
    private var liveRestaurantId = 0
    private var liveRestaurantName = ""
    private var liveRestaurantAddress = ""


    fun setId(id : Int) {
        liveRestaurantId = id
    }
    
    fun getId() : Int?{
        return liveRestaurantId
    }

    fun setName(name : String) {
        liveRestaurantName = name
    }

    fun getName() : String {
        return liveRestaurantName
    }

    fun setAddress(address: String){
        liveRestaurantAddress = address
    }

    fun getAddress() : String {
        return liveRestaurantAddress
    }
}