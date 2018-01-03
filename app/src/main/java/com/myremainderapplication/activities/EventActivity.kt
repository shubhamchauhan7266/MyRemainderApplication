package com.myremainderapplication.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.myremainderapplication.R
import com.myremainderapplication.interfaces.AppConstant
import com.myremainderapplication.utils.VolleySingletonClass
import org.json.JSONObject

class EventActivity : AppCompatActivity() {
    private lateinit var requestQueue: RequestQueue

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event)

        requestQueue = VolleySingletonClass.getInstance(this)!!
    }

    private fun sendEventNotification() {
        val jsonRequest = JsonObjectRequest(Request.Method.GET, AppConstant.SEND_NOTIFICATION_URL, getJsonBody(),
                Response.Listener<JSONObject> { response: JSONObject? ->

        },
                Response.ErrorListener { error: VolleyError? ->

        })

        requestQueue.add(jsonRequest)
    }

    private fun getJsonBody():JSONObject{
        var jsonObject=JSONObject()
        return jsonObject
    }
}
