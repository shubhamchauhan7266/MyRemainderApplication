package com.myremainderapplication.models

import java.io.Serializable

/**
 * Created by user on 7/12/17.
 */

class MemberInfoModel(val memberId: String, val memberName: String, val emailId: String, var phoneNumber: String, var password: String, var friendList: ArrayList<MemberIdNameModel>) : Serializable

class MemberIdNameModel(val memberId: String, val memberName: String):Serializable