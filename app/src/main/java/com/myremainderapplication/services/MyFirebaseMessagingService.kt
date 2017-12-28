package com.myremainderapplication.services

import com.google.firebase.messaging.FirebaseMessagingService
import android.content.ContentValues.TAG
import android.util.Log
import com.google.firebase.messaging.RemoteMessage
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.support.v4.app.NotificationCompat
import com.myremainderapplication.R
import com.myremainderapplication.activities.HomeActivity


/**
 * Created by Shubham Chauhan on 21/12/17.
 */
class MyFirebaseMessagingService : FirebaseMessagingService() {
    override fun onMessageReceived(remoteMessage: RemoteMessage?) {
       var title: String? =null
        var body:String?=null
        if (remoteMessage!!.data.size > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.data)
            title=remoteMessage.data!!.get("title")!!
            body=remoteMessage.data!!.get("body")!!
        }

        // Check if message contains a notification payload.
        if (remoteMessage.notification != null) {
            title=remoteMessage.notification!!.title!!
            body=remoteMessage.notification!!.body!!

            Log.d(TAG, "Message Notification Title: " + title)
            Log.d(TAG, "Message Notification Body: " + body)
        }
            sendNotification(title,body)
    }

    fun sendNotification(title:String?,body:String?){
        val intent = Intent(this, HomeActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(this, 1410,
                intent, PendingIntent.FLAG_ONE_SHOT)

        val notificationBuilder = NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.profile)
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        notificationManager.notify(1410, notificationBuilder.build())
    }
}