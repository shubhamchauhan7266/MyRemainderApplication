package com.myremainderapplication.utils

import android.content.Context
import android.util.Log

/**
 * <h1><font color="orange">SharedPreferencesUtils</font></h1>
 * this is a Utils class which contain sets of methods
 * these methods perform operation to save and retrieve data
 * Data is stored in fileSystem
 *
 * @author Shubham Chauhan
 */
class SharedPreferencesUtils {
    companion object {
        private val REGISTRATION_KEY = "REGISTRATION_KEY"
        private val REMINDER_SHARED_PREF = "REMINDER_SHARED_PREF"
        private val TAG = SharedPreferencesUtils::class.java.getSimpleName()

        /**
         * This method is used to get a registration key from fileSystem
         * @param context
         * @return  a registration key
         */
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

        /**
         * This method is used to store a registration key in filesystem which create every time when app is install
         * @param context
         * @param registrationKey
         */
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