package com.example.garam.takemehome_android.firebase

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.firebase.messaging.RemoteMessage
import org.json.JSONObject

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
        Log.e("message1",p0.notification?.title.toString())
        Log.e("message2",p0.notification?.body.toString())

        val title = p0.notification?.title.toString()
        val body = p0.notification?.body.toString()
        val style = NotificationCompat.BigTextStyle().bigText(p0.notification?.body.toString())

        when(title){
            "주문 요청" -> {
                val orderBody = JSONObject(body)
                val orderObject = orderBody.getJSONObject("menuNameCounts")
                val orderArray = orderObject.getJSONArray("menuNameCounts")

                val style = NotificationCompat.InboxStyle()

                for (i in 0 until orderArray.length()) {
                    style.addLine(orderArray.getJSONObject(i).getString("name")
                            + " ${orderArray.getJSONObject(i).getInt("count")}인분")
                }
                style.addLine("결제 금액 : ${orderBody.getInt("totalPrice")} 원")
                style.addLine("고객 주소 : ${orderBody.getString("customerAddress")}")


                val builder = createNotificationChannel("id", "name")
                    .setTicker("Ticker")
                    .setSmallIcon(android.R.drawable.ic_menu_search)
                    .setNumber(10)
                    .setAutoCancel(true)
                    .setContentTitle("주문 요청이 있습니다")
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setStyle(style)

                with(NotificationManagerCompat.from(this)) {
                    notify(0, builder.build())
                }
            }

            "주문이 취소 됐어요." -> {
                val builder = createNotificationChannel("id", "name")
                    .setTicker("Ticker")
                    .setSmallIcon(android.R.drawable.ic_menu_search)
                    .setNumber(10)
                    .setAutoCancel(true)
                    .setContentTitle("주문이 취소되었습니다")
                    .setContentText(body)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setStyle(style)

                with(NotificationManagerCompat.from(this)) {
                    notify(0, builder.build())
                }
            }

            "배달 요청!" -> {
                val builder = createNotificationChannel("id", "name")
                    .setTicker("Ticker")
                    .setSmallIcon(android.R.drawable.ic_menu_search)
                    .setNumber(10)
                    .setAutoCancel(true)
                    .setContentTitle("새로운 배달 요청이 있습니다")
                    .setContentText(body)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setStyle(style)

                with(NotificationManagerCompat.from(this)) {
                    notify(0, builder.build())
                }
            }

            "주문 접수 완료!" -> {
                val builder = createNotificationChannel("id", "name")
                    .setTicker("Ticker")
                    .setSmallIcon(android.R.drawable.ic_menu_search)
                    .setNumber(10)
                    .setAutoCancel(true)
                    .setContentTitle("주문이 접수되었습니다.")
                    .setContentText(body)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setStyle(style)

                with(NotificationManagerCompat.from(this)) {
                    notify(0, builder.build())
                }
            }

        }
    }

    private fun createNotificationChannel(id :String, name :String) : NotificationCompat.Builder{

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){

            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val channel = NotificationChannel(id, name, NotificationManager.IMPORTANCE_HIGH)

            manager.createNotificationChannel(channel)

            NotificationCompat.Builder(this, id)

        } else {
            NotificationCompat.Builder(this)
        }
    }
}