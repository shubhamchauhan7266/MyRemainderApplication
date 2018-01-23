package com.myremainderapplication.services

import android.app.IntentService
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import com.google.firebase.database.*
import com.myremainderapplication.R
import com.myremainderapplication.interfaces.AppConstant
import com.myremainderapplication.models.MemberFriendInfoModel
import com.myremainderapplication.models.MemberFullInfoModel
import com.myremainderapplication.models.MemberNotificationModel
import com.myremainderapplication.models.MemberShortInfoModel
import com.myremainderapplication.utils.ModelInfoUtils

/**
 * <h1><font color="orange">HandleFriendRequestService</font></h1>
 * this is a Intent Service class for update(add or remove friend) firebase data in background
 *
 * @author Shubham Chauhan
 */
class HandleFriendRequestService : IntentService("HandleFriendRequestService") {

    private var senderInfo: MemberShortInfoModel? = null

    private var receiverInfo: MemberFullInfoModel? = null

    private var senderId: String? = null

    private var receiverId: String? = null

    private lateinit var senderFriendList: ArrayList<MemberFriendInfoModel>
    private lateinit var receiverNotificationList: ArrayList<MemberNotificationModel>

    /**
     * this is an override method which handle all action done by user in notification and
     * also extract the sender and receiver information from the database
     * @param intent
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
                    val memberList = dataSnapshot?.child(AppConstant.MEMBERS)?.child(senderId)?.child(AppConstant.FRIEND_LIST)?.value as ArrayList<*>
                    val notificationList = dataSnapshot.child(AppConstant.MEMBERS)?.child(receiverId)?.child(AppConstant.NOTIFICATION_LIST)?.value as ArrayList<*>

                    senderFriendList = ModelInfoUtils.getFriendList(memberList)
                    receiverNotificationList = ModelInfoUtils.getNotificationList(notificationList)

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

    /**
     * This method is used to update data based on action of user.
     * if user click on Accept button then it goes to accept section and call addFriend to add user in friendList
     * if user click on Reject button then it goes to reject section
     * @param intent
     */
    private fun updateData(intent: Intent) {
        when (intent.action) {
            getString(R.string.accept) -> {
                if (senderId != null && receiverId != null) {
                    val databaseReceiverRef = FirebaseDatabase.getInstance().reference.child(AppConstant.MEMBERS).child(receiverId)
                    val databaseSenderRef = FirebaseDatabase.getInstance().reference.child(AppConstant.MEMBERS).child(senderId)

                    if (senderInfo != null && receiverInfo != null) {
                        val newReceiverFriendId = (receiverInfo?.currentFriendId!!.toInt() + 1).toString()
                        ModelInfoUtils.addFriend(databaseReceiverRef, newReceiverFriendId, senderInfo!!, ModelInfoUtils.FRIEND)
                        ModelInfoUtils.updateFriendStatus(databaseSenderRef, receiverId!!, senderFriendList, ModelInfoUtils.FRIEND)
                        ModelInfoUtils.updateNotificationType(databaseReceiverRef, senderId!!, receiverNotificationList, AppConstant.SIMPE_ALERT_TYPE, "Friend Request Accept")
                    }
                }
            }
            getString(R.string.reject) -> {
                if (senderId != null && receiverId != null) {
                    val databaseSenderRef = FirebaseDatabase.getInstance().reference.child(AppConstant.MEMBERS).child(senderId)
                    val databaseReceiverRef = FirebaseDatabase.getInstance().reference.child(AppConstant.MEMBERS).child(receiverId)

                    ModelInfoUtils.removeFriend(databaseSenderRef, receiverId!!, senderFriendList)
                    ModelInfoUtils.updateNotificationType(databaseReceiverRef, senderId!!, receiverNotificationList, AppConstant.SIMPE_ALERT_TYPE, "Friend Request Reject")
                }
            }
        }
    }
}