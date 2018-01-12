package com.myremainderapplication.services

import android.annotation.SuppressLint
import android.app.AlarmManager
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
import com.myremainderapplication.models.CalenderModel
import java.util.*
import android.widget.RemoteViews


/**
 * Created by Shubham Chauhan on 21/12/17.
 */
class MyFirebaseMessagingService : FirebaseMessagingService() {
    private val SIMPLE_NOTIFICATION_REQUEST = 1410
    override fun onMessageReceived(remoteMessage: RemoteMessage?) {
        var title: String? = null
        var body: String? = null
        val year: Int
        val month: Int
        val day: Int
        val hour: Int
        val minute: Int

        // Check if message contains a Alarm Data payload.
        if (remoteMessage!!.data.size > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.data)
            title = remoteMessage.data!!.get("title")!!
            body = remoteMessage.data!!.get("body")!!

            year = remoteMessage.data!!.get("year")!!.toInt()
            month = remoteMessage.data!!.get("month")!!.toInt()
            day = remoteMessage.data!!.get("day")!!.toInt()
            hour = remoteMessage.data!!.get("hour")!!.toInt()
            minute = remoteMessage.data!!.get("minute")!!.toInt()

            val calenderModel = CalenderModel(year, month, day, hour, minute)
            setEventAlarm(title, body, calenderModel)
        }

        // Check if message contains a notification payload.
        if (remoteMessage.notification != null) {
            title = remoteMessage.notification!!.title!!
            body = remoteMessage.notification!!.body!!

            Log.d(TAG, "Message Notification Title: " + title)
            Log.d(TAG, "Message Notification Body: " + body)
        }
        sendNotification(title, body)
    }

    @SuppressLint("WrongConstant")
    private fun setEventAlarm(title: String?, body: String?, calenderModel: CalenderModel) {

        val intent = Intent("com.myremainderapplication.activities.alarmactivity")
        val pendingIntent = PendingIntent.getActivity(baseContext, 0, intent, Intent.FLAG_ACTIVITY_NEW_TASK)
        val alarmManager: AlarmManager = baseContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val calendar = GregorianCalendar(calenderModel.year, calenderModel.month, calenderModel.day,
                calenderModel.hour, calenderModel.minute)
        val alarmTime = calendar.timeInMillis
        alarmManager.set(AlarmManager.RTC_WAKEUP, alarmTime, pendingIntent)
    }

    private fun sendNotification(title: String?, body: String?) {
        val intent = Intent(this, HomeActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(this, SIMPLE_NOTIFICATION_REQUEST,
                intent, PendingIntent.FLAG_ONE_SHOT)

        val notificationBuilder = NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.profile)
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        notificationManager.notify(SIMPLE_NOTIFICATION_REQUEST, notificationBuilder.build())
    }

    private fun sendFriendRequestNotification(title: String?, body: String?) {

        val remoteViews = RemoteViews(packageName, R.layout.customnotification)
        val intent = Intent(this, HomeActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(this, SIMPLE_NOTIFICATION_REQUEST,
                intent, PendingIntent.FLAG_ONE_SHOT)

        val notificationBuilder = NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.profile)
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        notificationManager.notify(SIMPLE_NOTIFICATION_REQUEST, notificationBuilder.build())
    }
}