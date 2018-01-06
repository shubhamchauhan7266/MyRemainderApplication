package com.myremainderapplication.models

import java.io.Serializable

/**
 * Created by user on 7/12/17.
 */

class MemberInfoModel : Serializable {
    val memberId: String
    val memberName: String
    val emailId: String
    var phoneNumber: String
    var password: String
    lateinit var friendList: ArrayList<MemberIdNameModel>

    constructor(memberId: String, memberName: String, emailId: String, phoneNumber: String, password: String, friendList: ArrayList<MemberIdNameModel>) {
        this.memberId = memberId
        this.memberName = memberName
        this.emailId = emailId
        this.phoneNumber = phoneNumber
        this.password = password
        this.friendList = friendList
    }
    constructor(memberId: String, memberName: String, emailId: String, phoneNumber: String, password: String) {
        this.memberId = memberId
        this.memberName = memberName
        this.emailId = emailId
        this.phoneNumber = phoneNumber
        this.password = password
    }
}


class MemberIdNameModel(val memberId: String, val memberName: String):Serializable