package com.myremainderapplication.services

import android.content.ContentValues
import android.util.Log
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.FirebaseInstanceIdService
import com.myremainderapplication.utils.SharedPreferencesUtils

/**
 * Created by user on 21/12/17.
 */
class MyFirebaseInstanceIdService : FirebaseInstanceIdService() {
    override fun onTokenRefresh() {
        // Get updated InstanceID token.
        val refreshedToken = FirebaseInstanceId.getInstance().token
        Log.d(ContentValues.TAG, "Refreshed token: " + refreshedToken!!)
        SharedPreferencesUtils.setRegistrationKey(this,refreshedToken.toString())
    }
}