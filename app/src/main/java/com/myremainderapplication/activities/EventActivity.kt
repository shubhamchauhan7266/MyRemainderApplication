package com.myremainderapplication.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.Snackbar
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.myremainderapplication.R
import com.myremainderapplication.interfaces.AppConstant
import com.myremainderapplication.utils.VolleySingletonClass
import org.json.JSONObject
import com.android.volley.AuthFailureError
import kotlinx.android.synthetic.main.activity_event.*
import kotlinx.android.synthetic.main.activity_sign_up.*


class EventActivity : AppCompatActivity() {
    private lateinit var requestQueue: RequestQueue

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event)

        requestQueue = VolleySingletonClass.getInstance(this)!!
        btSend.setOnClickListener{
            if(etTitle.text.toString().trim().length>0&&etDescription.text.toString().trim().length>0)
                  sendEventNotification(etTitle.text.toString(),etDescription.text.toString())
            else
                Snackbar.make(rootView,"Please fill all Field",Snackbar.LENGTH_SHORT).show()
        }
    }

    private fun sendEventNotification(title: String,body:String) {
        val jsonRequest =object :JsonObjectRequest(Request.Method.POST, AppConstant.SEND_NOTIFICATION_URL, getJsonBody(title,body),
                Response.Listener<JSONObject> { response: JSONObject? ->

        },
                Response.ErrorListener { error: VolleyError? ->

        }
        ){
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val params = HashMap<String, String>()
                params.put("Content-Type", "application/json")
                params.put("Authorization", "key=AIzaSyDKBMR1gRQrZhARcTvAhZb4PkvcNlLGGQE")

                return params
            }
        }
        requestQueue.add(jsonRequest)
    }

    private fun getJsonBody(title: String,body:String):JSONObject{
        val jsonObjectRequestParams=JSONObject()

        val jsonObjectData=JSONObject()
        jsonObjectData.put("title",title)
        jsonObjectData.put("body",body)

        jsonObjectRequestParams.put("data",jsonObjectData)
        jsonObjectRequestParams.put("to","eiArmxrjQ4M:APA91bGnntKK76Rfhz-si1F_vC9mLNP12138WlOsXeaiZmiFyknY9ZA-UQFg-6WwpD_rfijadptEIybHvSIzJLuIbaQtyDYjrMYwxtQV0KTuVGDZ2RT4FRrSY6ltoPRA35c67gk4oaRe")

        return jsonObjectRequestParams
    }
}
