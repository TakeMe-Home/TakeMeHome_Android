package com.example.garam.takemehome_android.customer

import androidx.lifecycle.ViewModel

class MenuSharedViewModel : ViewModel() {

    private var restaurantId = ArrayList<Int>()
    fun setRestaurantId(id: Int)
    {
        restaurantId.add(id)
    }

    fun getRestaurantId() : ArrayList<Int>{
        return restaurantId
    }
}