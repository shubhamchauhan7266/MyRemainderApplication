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

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
//        sendRegistrationToServer(refreshedToken)
    }

   /* private fun sendRegistrationToServer(refreshedToken: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }*/
}