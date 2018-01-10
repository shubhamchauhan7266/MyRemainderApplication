package com.myremainderapplication.utils

import android.content.Context
import android.util.Log

/**
 * Created by user on 10/1/18.
 */
class SharedPreferencesUtils {
    companion object {
        private val REGISTRATION_KEY = "REGISTRATION_KEY"
        private val REMINDER_SHARED_PREF = "REMINDER_SHARED_PREF"
        private val TAG = SharedPreferencesUtils::class.java.getSimpleName()


        fun getRegistrationKey(context: Context): String? {
            Log.v(TAG, "getRegistrationKey")
            try {
                val sharedPref = context.getSharedPreferences(REMINDER_SHARED_PREF, Context.MODE_PRIVATE)
                return sharedPref.getString(REGISTRATION_KEY, null)
            } catch (e: NullPointerException) {
                Log.e(TAG, "error")
                return null
            }
        }

        fun setRegistrationKey(context: Context, registrationKey: String?) {
            Log.v(TAG, "getRegistrationKey")
            try {
                val sharedPref = context.getSharedPreferences(REMINDER_SHARED_PREF, Context.MODE_PRIVATE)
                val editor = sharedPref.edit()
                editor.putString(REGISTRATION_KEY, registrationKey)
                editor.apply()
            } catch (e: NullPointerException) {
                Log.e(TAG, "error")
            }
        }


    }

}