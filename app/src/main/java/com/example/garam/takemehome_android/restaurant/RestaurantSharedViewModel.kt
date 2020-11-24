package com.example.garam.takemehome_android.restaurant

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class RestaurantSharedViewModel : ViewModel(){
    private var liveRestaurantId = 0

    fun setId(id : Int) {
        liveRestaurantId = id
        Log.e("라이브 레스토랑",liveRestaurantId.toString())
    }
    
    fun getId() : Int?{
        return liveRestaurantId
    }
}