package com.example.garam.takemehome_android.firebase

import android.content.Intent
import android.util.Log
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.firebase.messaging.*

class FirebaseMessagingServices : com.google.firebase.messaging.FirebaseMessagingService() {

    private lateinit var broadcaster: LocalBroadcastManager

    override fun onNewToken(p0: String) {
        super.onNewToken(p0)
        Log.e("FCM Token",p0)
    }

    override fun onCreate() {
        broadcaster = LocalBroadcastManager.getInstance(this)
    }

    override fun onMessageReceived(p0: RemoteMessage) {
        Log.e("FCM" , p0.data.toString())
        Log.e("message1",p0.notification?.title.toString())
        Log.e("message2",p0.notification?.body.toString())

        val intent = Intent("MyData")
        intent.putExtra("test",p0.notification?.title.toString())
        intent.putExtra("test2",p0.notification?.body.toString())

        broadcaster.sendBroadcast(intent)
    }

}