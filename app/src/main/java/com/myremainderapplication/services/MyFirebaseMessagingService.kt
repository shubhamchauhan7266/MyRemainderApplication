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
import com.myremainderapplication.interfaces.AppConstant


/**
 * Created by Shubham Chauhan on 21/12/17.
 */
class MyFirebaseMessagingService : FirebaseMessagingService() {
    override fun onMessageReceived(remoteMessage: RemoteMessage?) {
        var title: String?
        var body: String?
        val year: Int
        val month: Int
        val day: Int
        val hour: Int
        val minute: Int

        // Check if message contains a Alarm Data payload.
        if (remoteMessage!!.data.isNotEmpty()) {
            Log.d(TAG, "Message data payload: " + remoteMessage.data)
            var type=0
            if(remoteMessage.data.containsKey("type")){
               type= remoteMessage.data["type"]!!.toInt()
            }
            when(type){
                1->{
                    val senderId = remoteMessage.data!![AppConstant.SENDER_ID_KEY]!!
                    val receiverId = remoteMessage.data!![AppConstant.RECEIVER_ID_KEY]!!
                    val message = remoteMessage.data!!["message"]!!
                    sendFriendRequestNotification(senderId,receiverId,message)
                }
                2->{
                    title = remoteMessage.data!!["title"]!!
                    body = remoteMessage.data!!["body"]!!

                    year = remoteMessage.data!!["year"]!!.toInt()
                    month = remoteMessage.data!!["month"]!!.toInt()
                    day = remoteMessage.data!!["day"]!!.toInt()
                    hour = remoteMessage.data!!["hour"]!!.toInt()
                    minute = remoteMessage.data!!["minute"]!!.toInt()

                    val calenderModel = CalenderModel(year, month, day, hour, minute)
                    setEventAlarm(title, body, calenderModel)
                    sendNotification(title, body)
                }
            }
        }

        // Check if message contains a notification payload.
        if (remoteMessage.notification != null) {
            title = remoteMessage.notification!!.title!!
            body = remoteMessage.notification!!.body!!

            Log.d(TAG, "Message Notification Title: " + title)
            Log.d(TAG, "Message Notification Body: " + body)
            sendNotification(title, body)
        }
    }

    @SuppressLint("WrongConstant")
    private fun setEventAlarm(title: String?, body: String?, calenderModel: CalenderModel) {

        val intent = Intent("com.myremainderapplication.activities.alarmactivity")
        val pendingIntent = PendingIntent.getActivity(baseContext, 2, intent, Intent.FLAG_ACTIVITY_NEW_TASK)
        val alarmManager: AlarmManager = baseContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val calendar = GregorianCalendar(calenderModel.year, calenderModel.month, calenderModel.day,
                calenderModel.hour, calenderModel.minute)
        val alarmTime = calendar.timeInMillis
        alarmManager.set(AlarmManager.RTC_WAKEUP, alarmTime, pendingIntent)
    }

    private fun sendNotification(title: String?, body: String?) {
        val intent = Intent(this, HomeActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(this, AppConstant.SIMPLE_NOTIFICATION_REQUEST,
                intent, PendingIntent.FLAG_ONE_SHOT)

        val notificationBuilder = NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.profile)
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        notificationManager.notify(AppConstant.SIMPLE_NOTIFICATION_REQUEST, notificationBuilder.build())
    }

    private fun sendFriendRequestNotification(senderId:String,receiverId:String,message: String) {

        val acceptIntent=Intent(this,HandleFriendRequestService::class.java)
        acceptIntent.action = getString(R.string.accept)
        acceptIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        acceptIntent.putExtra(AppConstant.SENDER_ID_KEY,senderId)
        acceptIntent.putExtra(AppConstant.RECEIVER_ID_KEY,receiverId)

        val rejectIntent=Intent(this,HandleFriendRequestService::class.java)
        rejectIntent.action = getString(R.string.reject)
        rejectIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        val expendView = RemoteViews(packageName, R.layout.customnotification)
        expendView.setTextViewText(R.id.tvMessage,message)
        expendView.setOnClickPendingIntent(R.id.tvAccept, PendingIntent.getService(this,AppConstant.CUSTOM_NOTIFICATION_REQUEST,acceptIntent,PendingIntent.FLAG_ONE_SHOT))
        expendView.setOnClickPendingIntent(R.id.tvReject, PendingIntent.getService(this,AppConstant.CUSTOM_NOTIFICATION_REQUEST,rejectIntent,PendingIntent.FLAG_ONE_SHOT))

        val intent = Intent(this, HomeActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(this, AppConstant.CUSTOM_NOTIFICATION_REQUEST,
                intent, PendingIntent.FLAG_ONE_SHOT)

        val notificationBuilder = NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.profile)
                .setContentTitle("custom notification")
                .setContentText("testing message")
                .setTicker("custom notification")
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setCustomBigContentView(expendView)
                .setStyle(NotificationCompat.DecoratedCustomViewStyle())
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(AppConstant.CUSTOM_NOTIFICATION_REQUEST, notificationBuilder.build())
    }
}