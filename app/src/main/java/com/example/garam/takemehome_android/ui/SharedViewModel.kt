package com.example.garam.takemehome_android.ui

import androidx.lifecycle.ViewModel
import com.example.garam.takemehome_android.ui.map.LocationList
import com.example.garam.takemehome_android.ui.call.CallList

class SharedViewModel : ViewModel() {

    private val livePost = ArrayList<CallList>()
    private var liveRange = 500
    private var riderId = 0
    private val liveLocation = ArrayList<LocationList>()
    fun getData(): ArrayList<CallList> {
        return livePost
    }
    fun setData(callList: CallList){
        livePost.add(callList)
    }

    fun setRange(range: Int){
        liveRange = range
    }

    fun getRange() : Int {
        return liveRange
    }

    fun setLocation(loc: LocationList){
         liveLocation.add(loc)
    }

    fun getLocation(): ArrayList<LocationList>{
        return liveLocation
    }
    fun setRiderId(id : Int){
        riderId = id
    }
    fun getRiderId() : Int {
        return riderId
    }
}