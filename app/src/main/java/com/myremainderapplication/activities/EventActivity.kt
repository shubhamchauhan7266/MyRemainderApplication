package com.myremainderapplication.activities

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.view.View
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
import com.myremainderapplication.models.CalenderModel
import kotlinx.android.synthetic.main.activity_event.*
import kotlinx.android.synthetic.main.activity_sign_up.*
import java.util.*


class EventActivity : AppCompatActivity(), View.OnClickListener {


    private lateinit var requestQueue: RequestQueue

    private var year: Int = 0

    private var month: Int = 0

    private var day: Int = 0

    private var hour: Int = 0

    private var minute: Int = 0

    private var alarmYear: Int = 0

    private var alarmMonth: Int = 0

    private var alarmDay: Int = 0

    private var alarmHour: Int = 0

    private var alarmMinute: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event)

        val calender = Calendar.getInstance()
        year = calender.get(Calendar.YEAR)
        month = calender.get(Calendar.MONTH)
        day = calender.get(Calendar.DAY_OF_MONTH)
        hour = calender.get(Calendar.HOUR_OF_DAY)
        minute = calender.get(Calendar.MINUTE)



        requestQueue = VolleySingletonClass.getInstance(this)!!

        btSend.setOnClickListener(this)
        btDate.setOnClickListener(this)
        btTime.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when (view!!.id) {
            R.id.btDate -> {
                val datePickerDialog = DatePickerDialog(this@EventActivity, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                    // Display Selected date in textbox
                    btDate.setText(dayOfMonth.toString() + "/" + (monthOfYear + 1) + "/" + year)
                    alarmYear=year
                    alarmMonth=monthOfYear
                    alarmDay=dayOfMonth
                }, year, month, day)
                datePickerDialog.show()
            }
            R.id.btTime -> {
                val timePickerDialog = TimePickerDialog(this@EventActivity, TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                    btTime.setText("" + hourOfDay + ":" + minute)
                    alarmHour=hourOfDay
                    alarmMinute=minute

                }, hour, minute,true)
                timePickerDialog.show()
            }
            R.id.btSend -> {
                val calenderModel = CalenderModel(alarmYear,alarmMonth,alarmDay,alarmHour,alarmMinute)
                if (etTitle.text.toString().trim().length > 0 && etDescription.text.toString().trim().length > 0)
                    sendEventNotification(etTitle.text.toString(), etDescription.text.toString(), calenderModel)
                else
                    Snackbar.make(rootView, "Please fill all Field", Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    private fun sendEventNotification(title: String, body: String, calenderModel: CalenderModel) {
        val request = getJsonBody(title, body, calenderModel)
        val jsonRequest = object : JsonObjectRequest(Request.Method.POST, AppConstant.SEND_NOTIFICATION_URL, request,
                Response.Listener<JSONObject> { response: JSONObject? ->

                },
                Response.ErrorListener { error: VolleyError? ->

                }
        ) {
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

    private fun getJsonBody(title: String, body: String, calenderModel: CalenderModel): JSONObject {
        val jsonObjectRequestParams = JSONObject()

        val jsonObjectData = JSONObject()
        jsonObjectData.put("title", title)
        jsonObjectData.put("body", body)
        jsonObjectData.put("year", calenderModel.year)
        jsonObjectData.put("month", calenderModel.month)
        jsonObjectData.put("day", calenderModel.day)
        jsonObjectData.put("hour", calenderModel.hour)
        jsonObjectData.put("minute", calenderModel.minute)

        jsonObjectRequestParams.put("data", jsonObjectData)
        jsonObjectRequestParams.put("to", "eiArmxrjQ4M:APA91bGnntKK76Rfhz-si1F_vC9mLNP12138WlOsXeaiZmiFyknY9ZA-UQFg-6WwpD_rfijadptEIybHvSIzJLuIbaQtyDYjrMYwxtQV0KTuVGDZ2RT4FRrSY6ltoPRA35c67gk4oaRe")

        return jsonObjectRequestParams
    }
}
