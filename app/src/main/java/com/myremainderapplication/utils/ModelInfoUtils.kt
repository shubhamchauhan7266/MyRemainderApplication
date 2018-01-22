package com.myremainderapplication.utils

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
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
            val data = dataSnapshot?.child(AppConstant.MEMBERS)!!.child(memberId).value as HashMap<*, *>
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
            val data = dataSnapshot?.child(AppConstant.MEMBERS)!!.child(memberId).value as HashMap<*, *>
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
         * @return arraylist of MemberNotificationModel
         */
        fun getNotificationList(notificationDataList: ArrayList<*>): ArrayList<MemberNotificationModel> {
            val size = notificationDataList.size
            var index = 0
            val notificationList = ArrayList<MemberNotificationModel>()
            while (index < size) {
                val hashMap: HashMap<*, *> = notificationDataList[index] as HashMap<*, *>
                val title = hashMap[AppConstant.TITLE].toString()
                val body = hashMap[AppConstant.BODY].toString()
                notificationList.add(MemberNotificationModel(title, body))
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
        fun addFriend(databaseReference: DatabaseReference?, friendId: String, friendInfo: MemberShortInfoModel, friendStatus: Int) {
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
        fun removeFriend(databaseReference: DatabaseReference?, friendId: String, friendList: ArrayList<MemberFriendInfoModel>) {
            var index = -1
            for (friendInfo: MemberFriendInfoModel in friendList) {
                if (friendInfo.memberId == friendId) {
                    index = friendList.indexOf(friendInfo)
                    break
                }
            }
            val friendListId = (friendList.size - 2).toString()
            databaseReference?.child(AppConstant.FRIEND_LIST)?.child(index.toString())?.removeValue()

            val hasMapFriendId = HashMap<String, String>()
            hasMapFriendId.put(AppConstant.CURRENT_FRIEND_LIST_ID, friendListId)
            databaseReference?.updateChildren(hasMapFriendId as Map<String, Any>?)
        }

        /**
         * This method is used to update friend status
         * @param databaseReference - refrence to a friendList of Member
         * @param friendId - friendId
         * @param friendList - arraylist of all friends
         * @param friendStatus
         */
        fun updateFriendStatus(databaseReference: DatabaseReference?, friendId: String, friendList: ArrayList<MemberFriendInfoModel>, friendStatus: Int) {
            val hasMapFriendUserNode = HashMap<String, Any>()
            hasMapFriendUserNode.put(AppConstant.FRIEND_STATUS, friendStatus)
            var index = -1
            for (friendInfo: MemberFriendInfoModel in friendList) {
                if (friendInfo.memberId == friendId) {
                    index = friendList.indexOf(friendInfo)
                    break
                }
            }
            databaseReference?.child(AppConstant.FRIEND_LIST)?.child(index.toString())?.updateChildren(hasMapFriendUserNode as Map<String, Any>?)
        }
    }

}