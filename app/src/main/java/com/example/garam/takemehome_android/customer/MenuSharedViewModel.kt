package com.example.garam.takemehome_android.customer

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.JsonObject
import org.json.JSONObject
import java.util.*
import kotlin.collections.ArrayList

class MenuSharedViewModel : ViewModel() {

    private var lastPayPrice = 0
    private var lastTotalPrice = 0
    private var restaurantId = ArrayList<Int>()
    private var menuIdCount = Hashtable<Int,Int>()
    private var menuArray = ArrayList<JSONObject>()
    private var menuNameArray = ArrayList<JSONObject>()
    private var customerId = 0

    fun setCustomerId(id : Int) {
        customerId = id
    }

    fun getCustomerId() : Int {
        return customerId
    }

    fun setRestaurantId(id: Int)
    {
        restaurantId.add(id)
    }

    fun getRestaurantId() : ArrayList<Int>{
        return restaurantId
    }

    fun setCountInfo(menuId: Int, count: Int){
        when{
            menuIdCount[menuId] == null -> {
                menuIdCount[menuId] = count
            }
            else -> {
                menuIdCount[menuId] = menuIdCount[menuId]?.plus(count)
            }
        }
    }

    fun getCountInfo() : Int{
        return menuIdCount.size
    }

    fun setMenuNameArray(menuNameJson: JSONObject){
        menuNameArray.add(menuNameJson)
    }

    fun getMenuNameArray() : ArrayList<JSONObject>{
        return menuNameArray
    }

    fun setMenuArray(menuJson : JSONObject) {
        menuArray.add(menuJson)
    }

    fun getMenuArray() : ArrayList<JSONObject> {
        return menuArray
    }

    fun setLastPayPrice(money: Int) {
        lastPayPrice += money
    }

    fun getLastPayPrice() : Int{
        return lastPayPrice
    }

    fun setTotalPrice(price: Int){
        lastTotalPrice = price
    }

    fun getTotalPrice() : Int{
        return lastTotalPrice
    }
}