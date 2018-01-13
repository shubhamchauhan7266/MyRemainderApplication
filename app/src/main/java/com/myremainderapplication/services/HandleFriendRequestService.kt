package com.myremainderapplication.services

import android.app.IntentService
import android.content.Intent
import com.myremainderapplication.R

/**
 * Created by shubham on 13/1/18.
 */
class HandleFriendRequestService: IntentService("HandleFriendRequestService") {


    override fun onHandleIntent(intent: Intent?) {
        when(intent?.action){
            getString(R.string.accept)->{

            }
            getString(R.string.reject)->{

            }
        }
    }
}