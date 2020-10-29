package com.example.garam.takemehome_android.ui

import androidx.lifecycle.ViewModel
import com.example.garam.takemehome_android.ui.dashboard.location_List
import com.example.garam.takemehome_android.ui.home.call_List

class SharedViewModel : ViewModel() {

    private val livePost = ArrayList<call_List>()
    private var liveRange = 500
    private val liveLocation = ArrayList<location_List>()
    fun getData(): ArrayList<call_List> {
        return livePost
    }
    fun setData(callList: call_List){
        livePost.add(callList)
    }

    fun setRange(range: Int){
        liveRange = range
    }

    fun getRange() : Int {
        return liveRange
    }

    fun setLocation(loc: location_List){
         liveLocation.add(loc)
    }

    fun getLoaction(): ArrayList<location_List>{
        return liveLocation
    }

}