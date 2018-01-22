package com.myremainderapplication.models

import java.io.Serializable

/**
 * <h1><font color="orange">MemberDataModel</font></h1>
 * these are model classes for Members
 *
 * @author Shubham Chauhan
 */
class MemberFullInfoModel : Serializable {
    lateinit var memberId: String
    lateinit var currentFriendId: String
    lateinit var memberName: String
    lateinit var emailId: String
    lateinit var phoneNumber: String
    lateinit var gender: String
    lateinit var imagePath: String
    lateinit var registrationToken: String
    lateinit var password: String
}


class MemberShortInfoModel(val memberId: String, val memberName: String, val imagePath: String, val registrationToken: String) : Serializable

class MemberFriendInfoModel(val friendStatus: Int, val memberId: String, val memberName: String, val imagePath: String, val registrationToken: String) : Serializable

class MemberNotificationModel(val title: String, val body: String) : Serializable