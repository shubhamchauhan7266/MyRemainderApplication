package com.myremainderapplication.utils

import android.content.Context
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley

/**
 * <h1><font color="orange">VolleySingletonClass</font></h1>
 * this is a SingleTon class which is used to get instance of RequestQueue
 *
 * @author Shubham Chauhan
 */
class VolleySingletonClass(context: Context) {

    init {
        requestQueue = Volley.newRequestQueue(context)
    }

    companion object {
        private var requestQueue: RequestQueue? = null

        /**
         * You can get instance of VolleySingletonClass using this method
         *
         * @param context Activity context
         * @return RequestQueue(Volley) instance
         */
        fun getInstance(context: Context): RequestQueue? {

            if (requestQueue == null) {
                VolleySingletonClass(context)
            }
            return this.requestQueue
        }
    }
}