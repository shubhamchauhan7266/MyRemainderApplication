package com.myremainderapplication.utils

import com.google.firebase.database.DataSnapshot
import com.myremainderapplication.interfaces.AppConstant
import com.myremainderapplication.models.MemberFriendInfoModel
import com.myremainderapplication.models.MemberShortInfoModel
import com.myremainderapplication.models.MemberFullInfoModel
import com.myremainderapplication.models.MemberNotificationModel

class ModelInfoUtils {

    companion object {
        val FRIEND_REQUEST_SENT = 0
        val FRIEND = 1

        fun getMemberInfoModel(dataSnapshot: DataSnapshot?, memberId: String): MemberFullInfoModel {
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

        fun getFriendList(friendDataList: ArrayList<*>): ArrayList<MemberFriendInfoModel> {
            val size = friendDataList.size
            var index = 0
            val friendList = ArrayList<MemberFriendInfoModel>()
            while (index < size) {
                val hashMap: HashMap<*, *> = friendDataList[index] as HashMap<*, *>
                val friendStatus:Long = hashMap[AppConstant.FRIEND_STATUS] as Long
                val friendId = hashMap[AppConstant.MEMBER_ID].toString()
                val friendName = hashMap[AppConstant.MEMBER_NAME].toString()
                val imagePath = hashMap[AppConstant.IMAGE_PATH].toString()
                val registrationToken = hashMap[AppConstant.REGISTRATION_TOKEN].toString()
                friendList.add(MemberFriendInfoModel(friendStatus.toInt(), friendId, friendName, imagePath, registrationToken))
                index++
            }
            return friendList
        }

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
    }

}