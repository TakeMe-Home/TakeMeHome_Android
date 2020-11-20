package com.example.garam.takemehome_android.restaurant

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class RestaurantSharedViewModel : ViewModel(){
    private var liveRestaurantId = MutableLiveData<Int>()

    fun setId(id : Int) {
        liveRestaurantId.value = id
    }
    
    fun getId() : Int?{
        return liveRestaurantId.value
    }
}