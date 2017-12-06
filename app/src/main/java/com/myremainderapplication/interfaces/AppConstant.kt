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
        val ZIP = "Zip - "
        val EMAIL = "Email - "
        val MOBILE_NUMBER = "Mobile Number - "
        val NAME = "Name - "
    }


}
