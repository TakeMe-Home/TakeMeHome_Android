package com.example.garam.takemehome_android.customer

import androidx.lifecycle.ViewModel
import org.json.JSONObject
import java.util.*
import kotlin.collections.ArrayList

class MenuSharedViewModel : ViewModel() {

    private var restaurantId = ArrayList<Int>()
    private var menuIdCount = Hashtable<Int,Int>()
    private var menuArray = ArrayList<JSONObject>()

    fun setRestaurantId(id: Int)
    {
        restaurantId.add(id)
    }

    fun getRestaurantId() : ArrayList<Int>{
        return restaurantId
    }

    fun setCountInfo(menuId: Int, count: Int){
        menuIdCount[menuId] = count
    }

    fun getCountInfo() : Int{
        return menuIdCount.size
    }

    fun setMenuArray(menuJson : JSONObject) {
        menuArray.add(menuJson)
    }

    fun getMenuArray() : ArrayList<JSONObject> {
        return menuArray
    }


}