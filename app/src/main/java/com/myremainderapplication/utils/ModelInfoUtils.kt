package com.myremainderapplication.utils

import com.google.firebase.database.DataSnapshot
import com.myremainderapplication.interfaces.AppConstant
import com.myremainderapplication.models.MemberIdNameModel
import com.myremainderapplication.models.MemberInfoModel

class ModelInfoUtils {

    companion object {
        fun getMemberInfoModel(dataSnapshot: DataSnapshot?, memberId: String): MemberInfoModel {
            val data = dataSnapshot?.child(AppConstant.MEMBERS)!!.child(memberId).value as HashMap<*, *>
            val id = data[AppConstant.MEMBER_ID].toString()
            val name = data[AppConstant.MEMBER_NAME].toString()
            val phoneNumber = data[AppConstant.PHONE_NUMBER].toString()
            val emailId = data[AppConstant.EMAIL_ID].toString()
            val password = data[AppConstant.PASSWORD].toString()
            val friendDataList = data[AppConstant.FRIEND_LIST] as ArrayList<*>
            val friendList = getMemberIdNameModel(friendDataList)

            return MemberInfoModel(id, name, emailId, phoneNumber, password, friendList)
        }

        fun getMemberIdNameModel(friendDataList: ArrayList<*>): ArrayList<MemberIdNameModel> {
            val size = friendDataList.size
            var index = 0
            val friendList = ArrayList<MemberIdNameModel>()
            while (index < size) {
                val hashMap: HashMap<*, *> = friendDataList[index] as HashMap<*, *>
                val friendId = hashMap[AppConstant.MEMBER_ID].toString()
                val friendName = hashMap[AppConstant.MEMBER_NAME].toString()
                friendList.add(MemberIdNameModel(friendId, friendName))
                index++
            }
            return friendList
        }
    }

}