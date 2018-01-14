package com.myremainderapplication.interfaces

/**
 * <h1><font color="orange">AppConstant</font></h1>
 * Interface for Constant
 *
 * @author Shubham Chauhan
 */

interface AppConstant {
    companion object {
        val SPLASH_TIME_OUT = 3000
        val SIMPLE_NOTIFICATION_REQUEST = 10
        val CUSTOM_NOTIFICATION_REQUEST = 11
        val REQUEST_SELECT_IMAGE_FROM_ALBUM = 1
        val REQUEST_TAKE_PHOTO = 2

        val APP_ID = "1:303119189109:android:bf9132e50cbefbce"
        val SENDER_ID = "303119189109"
        val PROJECT_ID = "myremainderapplication"
        val LEGACY_SERVER_KEY = "AIzaSyDKBMR1gRQrZhARcTvAhZb4PkvcNlLGGQE"
        val SERVER_KEY="AAAARpNPxHU:APA91bHdOGD8-ZACgGLYbgqFEzzXokqaDmF-uFRdAW9u0iGP0Rcm5HHWAOjZ50oOPOCs_o6bYczOYR9QfehSvokQKmMwfG3EAsU3iSpsXboeYrjN-JkhdVeSYJ4iDnywU_wxeAi6yK75"
        val Authorization = "key="+LEGACY_SERVER_KEY

        val NAME_VALIDATION_ERROR = "please enter name"
        val MOBILE_NUMBER_VALIDATION_ERROR = "please enter mobile number"
        val NOT_VALID_MOBILE_NUMBER_ERROR = "please enter valid mobile number"
        val EMAIL_ID_VALIDATION_ERROR = "please enter email id"
        val NOT_VALID_EMAIL_ID_ERROR = "please enter valid email id"
        val PASSWORD_VALIDATION_ERROR = "please enter Password"
        val CONFIRM_PASSWORD_VALIDATION_ERROR = "please enter Confirm Password"
        val ZIP_VALIDATION_ERROR = "please enter zip"
        val PASSWORD_NOT_MATCH_ERROR = "Password and Confirm Password not matched!!!"
        val EMAIL_PATTERN = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"


        val MEMBER_ID = "id"
        val EMAIL_ID = "email"
        val PHONE_NUMBER = "phoneNo"
        val MEMBER_NAME = "name"
        val GENDER = "gender"
        val MEMBERS = "members"
        val MEMBERS_LIST = "membersList"
        val FRIEND_LIST = "friendList"
        val NOTIFICATION_LIST="notificationList"
        val CURRENT_MEMBER_ID = "currentMemberId"
        val CURRENT_MEMBER_LIST_ID = "currentMemberListId"
        val CURRENT_FRIEND_LIST_ID = "currentFriendId"
        val PASSWORD = "password"
        val REGISTRATION_TOKEN="registrationToken"
        val IMAGE_PATH="imagePath"
        val TITLE="title"
        val BODY="body"
        val SENDER_ID_KEY = "senderId"
        val RECEIVER_ID_KEY = "receiverId"

        val SEND_NOTIFICATION_URL = "https://fcm.googleapis.com/fcm/send"
    }


}
