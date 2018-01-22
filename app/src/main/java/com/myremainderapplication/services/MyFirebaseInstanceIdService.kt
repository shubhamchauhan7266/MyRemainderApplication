package com.myremainderapplication.services

import android.content.ContentValues
import android.util.Log
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.FirebaseInstanceIdService
import com.myremainderapplication.utils.SharedPreferencesUtils

/**
 * <h1><font color="orange">MyFirebaseInstanceIdService</font></h1>
 * this is a Service class which extends FirebaseInstanceIdService
 * this service class is used to get refreshedToken when app is installed
 *
 * @author Shubham Chauhan
 */
class MyFirebaseInstanceIdService : FirebaseInstanceIdService() {
    override fun onTokenRefresh() {
        // Get updated InstanceID token.
        val refreshedToken = FirebaseInstanceId.getInstance().token
        Log.d(ContentValues.TAG, "Refreshed token: " + refreshedToken!!)
        SharedPreferencesUtils.setRegistrationKey(this, refreshedToken.toString())
    }
}