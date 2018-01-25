package com.myremainderapplication.utils

import android.content.Context
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.myremainderapplication.interfaces.AppConstant
import com.myremainderapplication.models.MemberFriendInfoModel
import com.myremainderapplication.models.MemberShortInfoModel
import com.myremainderapplication.models.MemberFullInfoModel
import com.myremainderapplication.models.MemberNotificationModel

/**
 * <h1><font color="orange">ModelInfoUtils</font></h1>
 * this is a Utils class which contain sets of methods
 * these methods perform operation on firebase data
 *
 * @author Shubham Chauhan
 */
class ModelInfoUtils {

    companion object {
        val FRIEND_REQUEST_SENT = 0
        val FRIEND = 1

        /**
         * This method is used to get a full information of member
         * @param dataSnapshot
         * @param memberId
         * @return a oobject of MemberFullInfoModel
         */
        fun getMemberFullInfoModel(dataSnapshot: DataSnapshot?, memberId: String): MemberFullInfoModel {
            val memberInfoModel = MemberFullInfoModel()
            val data = dataSnapshot?.child(memberId)?.value as HashMap<*, *>
            memberInfoModel.memberId = data[AppConstant.MEMBER_ID].toString()
            memberInfoModel.currentFriendId = data[AppConstant.CURRENT_FRIEND_LIST_ID].toString()
            memberInfoModel.memberName = data[AppConstant.MEMBER_NAME].toString()
            memberInfoModel.gender = data[AppConstant.GENDER].toString()
            memberInfoModel.phoneNumber = data[AppConstant.PHONE_NUMBER].toString()
            memberInfoModel.emailId = data[AppConstant.EMAIL_ID].toString()
            memberInfoModel.password = data[AppConstant.PASSWORD].toString()
            memberInfoModel.registrationToken = data[AppConstant.REGISTRATION_TOKEN].toString()
            memberInfoModel.imagePath = data[AppConstant.IMAGE_PATH].toString()
            return memberInfoModel
        }

        /**
         * This method is used to get a short information of member
         * @param dataSnapshot
         * @param memberId
         * @return a oobject of MemberShortInfoModel
         */
        fun getMemberShortInfoModel(dataSnapshot: DataSnapshot?, memberId: String): MemberShortInfoModel {
            val data = dataSnapshot?.child(memberId)?.value as HashMap<*, *>
            val memberName = data[AppConstant.MEMBER_NAME].toString()
            val imagePath = data[AppConstant.IMAGE_PATH].toString()
            val registrationToken = data[AppConstant.REGISTRATION_TOKEN].toString()
            return MemberShortInfoModel(memberId, memberName, imagePath, registrationToken)
        }

        /**
         * This method is used to get all members in a list
         * @param memberDataList
         * @return arraylist of MemberShortInfoModel
         */
        fun getMemberList(memberDataList: ArrayList<*>): ArrayList<MemberShortInfoModel> {
            val size = memberDataList.size
            var index = 0
            val memberList = ArrayList<MemberShortInfoModel>()

            while (index < size) {
                val hashMap: HashMap<*, *> = memberDataList[index] as HashMap<*, *>
                val friendId = hashMap[AppConstant.MEMBER_ID].toString()
                val friendName = hashMap[AppConstant.MEMBER_NAME].toString()
                val imagePath = hashMap[AppConstant.IMAGE_PATH].toString()
                val registrationToken = hashMap[AppConstant.REGISTRATION_TOKEN].toString()
                memberList.add(MemberShortInfoModel(friendId, friendName, imagePath, registrationToken))
                index++
            }
            return memberList
        }

        /**
         * This method is used to get all friends in a list
         * @param friendDataList
         * @return arraylist of MemberFriendInfoModel
         */
        fun getFriendList(friendDataList: ArrayList<*>): ArrayList<MemberFriendInfoModel> {
            val size = friendDataList.size
            var index = 0
            val friendList = ArrayList<MemberFriendInfoModel>()

            while (index < size) {
                val hashMap: HashMap<*, *> = friendDataList[index] as HashMap<*, *>
                val friendStatus: Long = hashMap[AppConstant.FRIEND_STATUS] as Long
                val friendId = hashMap[AppConstant.MEMBER_ID].toString()
                val friendName = hashMap[AppConstant.MEMBER_NAME].toString()
                val imagePath = hashMap[AppConstant.IMAGE_PATH].toString()
                val registrationToken = hashMap[AppConstant.REGISTRATION_TOKEN].toString()
                friendList.add(MemberFriendInfoModel(friendStatus.toInt(), friendId, friendName, imagePath, registrationToken))
                index++
            }
            return friendList
        }

        /**
         * This method is used to get all notification in a list
         * @param notificationDataList
         * @param type
         * @return arraylist of MemberNotificationModel
         */
        fun getNotificationList(notificationDataList: ArrayList<*>): ArrayList<MemberNotificationModel> {
            val size = notificationDataList.size
            var index = 0
            val notificationList = ArrayList<MemberNotificationModel>()

            while (index < size) {
                val hashMap: HashMap<*, *> = notificationDataList[index] as HashMap<*, *>
                val type = hashMap[AppConstant.NOTIFICATION_TYPE] as Long
                val title = hashMap[AppConstant.TITLE].toString()
                val body = hashMap[AppConstant.BODY].toString()
                val senderId = hashMap[AppConstant.SENDER_ID_KEY].toString()
                val receiverId = hashMap[AppConstant.RECEIVER_ID_KEY].toString()
                notificationList.add(MemberNotificationModel(type.toInt(), title, body, senderId, receiverId))
                index++
            }
            return notificationList
        }

        /**
         * This method is used to add friend data in FriendList
         * @param  databaseReference  - refrence to a friendList of Member
         * @param friendId - new FriendList id at which friend will add
         * @param friendInfo - information of friend
         * @param friendStatus - friend status like FRIEND , FRIEND_REQUEST_SENT
         */
        fun addFriend(databaseReference: DatabaseReference?, friendId: String,
                      friendInfo: MemberShortInfoModel, friendStatus: Int) {
            val hasMapFriendUserNode = HashMap<String, Any>()
            hasMapFriendUserNode.put(AppConstant.FRIEND_STATUS, friendStatus)
            hasMapFriendUserNode.put(AppConstant.MEMBER_ID, friendInfo.memberId)
            hasMapFriendUserNode.put(AppConstant.MEMBER_NAME, friendInfo.memberName)
            hasMapFriendUserNode.put(AppConstant.IMAGE_PATH, friendInfo.imagePath)
            hasMapFriendUserNode.put(AppConstant.REGISTRATION_TOKEN, friendInfo.registrationToken)

            val hasMapFriendListNode = HashMap<String, HashMap<String, Any>>()
            hasMapFriendListNode.put(friendId, hasMapFriendUserNode)
            databaseReference?.child(AppConstant.FRIEND_LIST)?.updateChildren(hasMapFriendListNode as Map<String, Any>?)

            val hasMapFriendId = HashMap<String, String>()
            hasMapFriendId.put(AppConstant.CURRENT_FRIEND_LIST_ID, friendId)
            databaseReference?.updateChildren(hasMapFriendId as Map<String, Any>?)
        }

        /**
         * This method is used to remove friend data from FriendList
         * @param databaseReference - refrence to a friendList of Member
         * @param friendId - friendId
         * @param friendList - arraylist of all friends
         */
        fun removeFriend(databaseReference: DatabaseReference?, friendId: String,
                         friendList: ArrayList<MemberFriendInfoModel>) {
            var index = -1
            for (friendInfo: MemberFriendInfoModel in friendList) {
                if (friendInfo.memberId == friendId) {
                    index = friendList.indexOf(friendInfo)
                    break
                }
            }

            if (index != -1) {
                val hasMapFriendId = HashMap<String, String>()
                val friendListId = (friendList.size - 2).toString()
                hasMapFriendId.put(AppConstant.CURRENT_FRIEND_LIST_ID, friendListId)
                databaseReference?.updateChildren(hasMapFriendId as Map<String, Any>?)

                databaseReference?.child(AppConstant.FRIEND_LIST)?.child(index.toString())?.removeValue()
            }
        }

        /**
         * This method is used to update friend status
         * @param databaseReference - refrence to a friendList of Member
         * @param friendId - friendId
         * @param friendList - arraylist of all friends
         * @param friendStatus
         */
        fun updateFriendStatus(databaseReference: DatabaseReference?, friendId: String,
                               friendList: ArrayList<MemberFriendInfoModel>, friendStatus: Int) {
            val hasMapFriendUserNode = HashMap<String, Any>()
            hasMapFriendUserNode.put(AppConstant.FRIEND_STATUS, friendStatus)
            var index = -1
            for (friendInfo: MemberFriendInfoModel in friendList) {
                if (friendInfo.memberId == friendId) {
                    index = friendList.indexOf(friendInfo)
                    break
                }
            }

            if (index != -1)
                databaseReference?.child(AppConstant.FRIEND_LIST)?.child(index.toString())?.
                        updateChildren(hasMapFriendUserNode as Map<String, Any>?)
        }

        /**
         * This method is used to add notification data in FriendList
         * @param  databaseReference  - refrence to a friendList of Member
         * @param notificationId - new Notification id at which notification will add
         * @param notificationInfo - information of notification
         */
        fun addNotification(databaseReference: DatabaseReference?, notificationId: String,
                            notificationInfo: MemberNotificationModel) {
            val hasMapNotificationNode = HashMap<String, Any>()
            hasMapNotificationNode.put(AppConstant.NOTIFICATION_TYPE, notificationInfo.type)
            hasMapNotificationNode.put(AppConstant.TITLE, notificationInfo.title)
            hasMapNotificationNode.put(AppConstant.BODY, notificationInfo.body)
            hasMapNotificationNode.put(AppConstant.SENDER_ID_KEY, notificationInfo.senderId)
            hasMapNotificationNode.put(AppConstant.RECEIVER_ID_KEY, notificationInfo.receiverId)

            val hasMapNotificationListNode = HashMap<String, HashMap<String, Any>>()
            hasMapNotificationListNode.put(notificationId, hasMapNotificationNode)
            databaseReference?.child(AppConstant.NOTIFICATION_LIST)?.
                    updateChildren(hasMapNotificationListNode as Map<String, Any>?)

            val hasMapNotificationId = HashMap<String, String>()
            hasMapNotificationId.put(AppConstant.CURRENT_NOTIFICATION_ID, notificationId)
            databaseReference?.updateChildren(hasMapNotificationId as Map<String, Any>?)
        }

        /**
         * This method is used to update notification type
         * @param databaseReference - refrence to a friendList of Member
         * @param receiverId - receiverId
         * @param notificationList - arraylist of all notification
         * @param notificationType
         * @param message
         */
        fun updateNotificationType(databaseReference: DatabaseReference?, senderId: String,
                                   notificationList: ArrayList<MemberNotificationModel>, notificationType: Int, message: String) {
            val hasMapNotificationNode = HashMap<String, Any>()
            hasMapNotificationNode.put(AppConstant.NOTIFICATION_TYPE, notificationType)
            hasMapNotificationNode.put(AppConstant.BODY, message)
            var index = -1
            for (notificationInfo: MemberNotificationModel in notificationList) {
                if (notificationInfo.senderId == senderId && notificationInfo.type==AppConstant.FRIEND_REQUEST_TYPE) {
                    index = notificationList.indexOf(notificationInfo)
                    break
                }
            }

            if (index != -1)
                databaseReference?.child(AppConstant.NOTIFICATION_LIST)?.child(index.toString())?.
                        updateChildren(hasMapNotificationNode as Map<String, Any>?)
        }

        fun updateRegistrationToken(context: Context, memberId: String, memberListId: String) {
            val databaseMemberRef = FirebaseDatabase.getInstance().reference.child(AppConstant.MEMBERS).child(memberId)
            val databaseMemberListRef = FirebaseDatabase.getInstance().reference.child(AppConstant.MEMBERS).child(AppConstant.MEMBERS_LIST)

            val hashMap = HashMap<String, String>()
            hashMap.put(AppConstant.REGISTRATION_TOKEN, SharedPreferencesUtils.getRegistrationKey(context).toString())

            databaseMemberRef.updateChildren(hashMap as Map<String, Any>?)
            databaseMemberListRef.child(memberListId).updateChildren(hashMap as Map<String, Any>?)
        }
    }

}