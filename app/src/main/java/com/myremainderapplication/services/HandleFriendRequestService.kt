package com.myremainderapplication.services

import android.app.IntentService
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import com.google.firebase.database.*
import com.myremainderapplication.R
import com.myremainderapplication.interfaces.AppConstant
import com.myremainderapplication.models.MemberFullInfoModel
import com.myremainderapplication.models.MemberShortInfoModel
import com.myremainderapplication.utils.ModelInfoUtils

/**
 * Created by shubham on 13/1/18.
 */
class HandleFriendRequestService : IntentService("HandleFriendRequestService") {

    private var senderInfo: MemberShortInfoModel? = null

    private var receiverInfo: MemberFullInfoModel? = null

    private var senderId: String? = null

    private var receiverId: String? = null

    /*
     * this is an override method which handle all action done by user in notification and
     * also extract the sender and receiver information from the database
     */
    override fun onHandleIntent(intent: Intent?) {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancel(AppConstant.CUSTOM_NOTIFICATION_REQUEST)
        sendBroadcast(Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS))

        var updateRequired = true
        if (intent!!.hasExtra(AppConstant.SENDER_ID_KEY) && intent.hasExtra(AppConstant.RECEIVER_ID_KEY)) {
            senderId = intent.getStringExtra(AppConstant.SENDER_ID_KEY)
            receiverId = intent.getStringExtra(AppConstant.RECEIVER_ID_KEY)
            val database = FirebaseDatabase.getInstance().reference
            database.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot?) {
                    senderInfo = ModelInfoUtils.getMemberShortInfoModel(dataSnapshot, senderId!!)
                    receiverInfo = ModelInfoUtils.getMemberFullInfoModel(dataSnapshot, receiverId!!)
                    if (updateRequired) {
                        updateData(intent)
                        updateRequired = false
                    }
                }

                override fun onCancelled(dataSnapshot: DatabaseError?) {
                }

            })
        }
    }

    /*
     * This method is used to update data based on action of user.
     * if user click on Accept button then it goes to accept section and call updateFriendList to add user in friendList
     * if user click on Reject button then it goes to reject section
     */
    private fun updateData(intent: Intent) {
        when (intent.action) {
            getString(R.string.accept) -> {
                if (receiverId != null) {
                    val databaseReceiverRef = FirebaseDatabase.getInstance().reference.child(AppConstant.MEMBERS).child(receiverId)

                    if (senderInfo != null && receiverInfo != null) {
                        val newReceiverFriendId = (receiverInfo?.currentFriendId!!.toInt() + 1).toString()
                        ModelInfoUtils.updateFriendList(databaseReceiverRef, newReceiverFriendId, senderInfo!!, ModelInfoUtils.FRIEND)
                    }
                }
            }
            getString(R.string.reject) -> {

            }
        }
    }

}