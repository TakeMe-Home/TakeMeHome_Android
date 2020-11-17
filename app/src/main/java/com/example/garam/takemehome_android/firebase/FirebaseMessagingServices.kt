package com.example.garam.takemehome_android.firebase

import android.util.Log
import com.google.firebase.messaging.*

class FirebaseMessagingServices : com.google.firebase.messaging.FirebaseMessagingService() {

    override fun onNewToken(p0: String) {
        super.onNewToken(p0)
        Log.e("FCM Token",p0)
    }

    override fun onMessageReceived(p0: RemoteMessage) {

    }
}