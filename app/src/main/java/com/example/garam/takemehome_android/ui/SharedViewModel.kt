package com.example.garam.takemehome_android.ui

import androidx.lifecycle.ViewModel
import com.example.garam.takemehome_android.ui.call.CallList

class SharedViewModel : ViewModel() {

    private val livePost = ArrayList<CallList>()

    private var riderId = 0

    fun getData(): ArrayList<CallList> {
        return livePost
    }

    fun setData(callList: CallList){
        livePost.add(callList)
    }

    fun setRiderId(id : Int){
        riderId = id
    }

    fun getRiderId() : Int {
        return riderId
    }
}