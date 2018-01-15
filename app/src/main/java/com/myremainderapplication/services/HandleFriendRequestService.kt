package com.myremainderapplication.services

import android.app.IntentService
import android.content.Intent
import com.google.firebase.database.*
import com.myremainderapplication.R
import com.myremainderapplication.interfaces.AppConstant
import com.myremainderapplication.models.MemberFullInfoModel
import com.myremainderapplication.utils.ModelInfoUtils

/**
 * Created by shubham on 13/1/18.
 */
class HandleFriendRequestService : IntentService("HandleFriendRequestService") {

    private var senderInfo: MemberFullInfoModel? = null

    private var receiverInfo: MemberFullInfoModel? = null

    private var senderId: String? = null

    private var receiverId: String? = null

    override fun onHandleIntent(intent: Intent?) {
        var updateRequired = true
        if (intent!!.hasExtra(AppConstant.SENDER_ID_KEY) && intent.hasExtra(AppConstant.RECEIVER_ID_KEY)) {
            senderId = intent.getStringExtra(AppConstant.SENDER_ID_KEY)
            receiverId = intent.getStringExtra(AppConstant.RECEIVER_ID_KEY)
            val database = FirebaseDatabase.getInstance().reference
            database.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot?) {
                    senderInfo = ModelInfoUtils.getMemberInfoModel(dataSnapshot, senderId!!)
                    receiverInfo = ModelInfoUtils.getMemberInfoModel(dataSnapshot, receiverId!!)
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

    private fun updateData(intent: Intent) {
        when (intent.action) {
            getString(R.string.accept) -> {
                if (senderId != null && receiverId != null) {
                    val databaseSenderRef = FirebaseDatabase.getInstance().reference.child(AppConstant.MEMBERS).child(senderId)
                    val databaseReceiverRef = FirebaseDatabase.getInstance().reference.child(AppConstant.MEMBERS).child(receiverId)

                    if (senderInfo != null && receiverInfo != null) {
                        val newSenderFriendId = (senderInfo?.currentFriendId!!.toInt() + 1).toString()
                        val newReceiverFriendId = (receiverInfo?.currentFriendId!!.toInt() + 1).toString()

                        updateFriendList(databaseSenderRef, newSenderFriendId, receiverInfo!!)
                        updateFriendList(databaseReceiverRef, newReceiverFriendId, senderInfo!!)
                    }
                }
            }
            getString(R.string.reject) -> {

            }
        }
    }

    private fun updateFriendList(databaseReference: DatabaseReference?, friendId: String, friendInfo: MemberFullInfoModel) {
        val hasMapFriendUserNode = HashMap<String, String>()
        hasMapFriendUserNode.put(AppConstant.MEMBER_ID, friendInfo.memberId)
        hasMapFriendUserNode.put(AppConstant.MEMBER_NAME, friendInfo.memberName)
        hasMapFriendUserNode.put(AppConstant.IMAGE_PATH, friendInfo.imagePath)
        hasMapFriendUserNode.put(AppConstant.REGISTRATION_TOKEN, friendInfo.registrationToken)

        val hasMapFriendListNode = HashMap<String, HashMap<String, String>>()
        hasMapFriendListNode.put(friendId, hasMapFriendUserNode)
        databaseReference?.child(AppConstant.FRIEND_LIST)?.updateChildren(hasMapFriendListNode as Map<String, Any>?)

        val hasMapFriendId = HashMap<String, String>()
        hasMapFriendId.put(AppConstant.CURRENT_FRIEND_LIST_ID, friendId)
        databaseReference?.updateChildren(hasMapFriendId as Map<String, Any>?)
    }
}