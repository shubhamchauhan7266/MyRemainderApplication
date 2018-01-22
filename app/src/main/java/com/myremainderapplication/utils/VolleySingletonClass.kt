package com.myremainderapplication.utils

import android.content.Context
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley

/**
 * Created by user on 3/1/18.
 */
class VolleySingletonClass(context: Context) {

    init {
        requestQueue = Volley.newRequestQueue(context)
    }

    /**
     * You can get instance of VolleySingletonClass using this method
     *
     * @param context Activity context
     * @return RequestQueue(Volley) instance
     */
    companion object {
        private var requestQueue: RequestQueue? = null

        fun getInstance(context: Context): RequestQueue? {

            if (requestQueue == null) {
                VolleySingletonClass(context)
            }
            return this.requestQueue
        }
    }
}