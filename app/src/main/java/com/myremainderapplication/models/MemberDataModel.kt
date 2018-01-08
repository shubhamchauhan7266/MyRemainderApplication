package com.myremainderapplication.models

import java.io.Serializable

/**
 * Created by user on 7/12/17.
 */

class MemberFullInfoModel() : Serializable {
    lateinit var  memberId: String
    lateinit var  memberName: String
    lateinit var  emailId: String
    lateinit var phoneNumber: String
    lateinit var gender: String
    lateinit var imagePath: String
    lateinit var registrationToken:String
    lateinit var password: String
    lateinit var friendList: ArrayList<MemberShortInfoModel>
    lateinit var notificationList:ArrayList<MemberNotificationModel>
}


class MemberShortInfoModel(val memberId: String, val memberName: String, val imagePath:String, val registrationToken:String):Serializable

class MemberNotificationModel(val messageId:String,val title:String,val body:String)