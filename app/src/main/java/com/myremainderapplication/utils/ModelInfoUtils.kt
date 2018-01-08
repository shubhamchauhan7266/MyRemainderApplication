package com.myremainderapplication.utils

import com.google.firebase.database.DataSnapshot
import com.myremainderapplication.interfaces.AppConstant
import com.myremainderapplication.models.MemberShortInfoModel
import com.myremainderapplication.models.MemberFullInfoModel
import com.myremainderapplication.models.MemberNotificationModel

class ModelInfoUtils {

    companion object {
        fun getMemberInfoModel(dataSnapshot: DataSnapshot?, memberId: String): MemberFullInfoModel {
            val memberInfoModel= MemberFullInfoModel()
            val data = dataSnapshot?.child(AppConstant.MEMBERS)!!.child(memberId).value as HashMap<*, *>
            memberInfoModel.memberId = data[AppConstant.MEMBER_ID].toString()
            memberInfoModel.memberName  = data[AppConstant.MEMBER_NAME].toString()
            memberInfoModel.phoneNumber = data[AppConstant.PHONE_NUMBER].toString()
            memberInfoModel.emailId = data[AppConstant.EMAIL_ID].toString()
            memberInfoModel.password = data[AppConstant.PASSWORD].toString()
            memberInfoModel.registrationToken=data[AppConstant.REGISTRATION_TOKEN].toString()
            memberInfoModel.imagePath=data[AppConstant.IMAGE_PATH].toString()
             return memberInfoModel
        }

        fun getMemberListModel(memberDataList: ArrayList<*>): ArrayList<MemberShortInfoModel> {
            val size = memberDataList.size
            var index = 0
            val memberList = ArrayList<MemberShortInfoModel>()
            while (index < size) {
                val hashMap: HashMap<*, *> = memberDataList[index] as HashMap<*, *>
                val friendId = hashMap[AppConstant.MEMBER_ID].toString()
                val friendName = hashMap[AppConstant.MEMBER_NAME].toString()
                val imagePath=hashMap[AppConstant.IMAGE_PATH].toString()
                val registrationToken=hashMap[AppConstant.REGISTRATION_TOKEN].toString()
                memberList.add(MemberShortInfoModel(friendId, friendName,imagePath,registrationToken))
                index++
            }
            return memberList
        }

        fun getMemberNotificationModel(notificationDataList: ArrayList<*>): ArrayList<MemberNotificationModel> {
            val size = notificationDataList.size
            var index = 0
            val notificationList = ArrayList<MemberNotificationModel>()
            while (index < size) {
                val hashMap: HashMap<*, *> = notificationDataList[index] as HashMap<*, *>
                val messageId = hashMap[AppConstant.MESSAGE_ID].toString()
                val title = hashMap[AppConstant.TITLE].toString()
                val body=hashMap[AppConstant.BODY].toString()
                notificationList.add(MemberNotificationModel(messageId,title,body))
                index++
            }
            return notificationList
        }
    }

}