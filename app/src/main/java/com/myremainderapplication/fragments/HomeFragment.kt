package com.myremainderapplication.fragments

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.android.volley.*
import com.android.volley.toolbox.JsonObjectRequest
import com.google.firebase.database.*

import com.myremainderapplication.R
import com.myremainderapplication.adapters.MemberListAdapter
import com.myremainderapplication.interfaces.AppConstant
import com.myremainderapplication.models.MemberShortInfoModel
import com.myremainderapplication.utils.ModelInfoUtils
import com.myremainderapplication.utils.SharedPreferencesUtils
import com.myremainderapplication.utils.VolleySingletonClass
import kotlinx.android.synthetic.main.fragment_home.view.*
import org.json.JSONObject


/**
 * A simple [Fragment] subclass.
 */
class HomeFragment : Fragment(), MemberListAdapter.IMemberListAdapterCallBack {

    private var memberList: ArrayList<MemberShortInfoModel>? = null
    private lateinit var memberListAdapter: MemberListAdapter
    private lateinit var requestQueue: RequestQueue
    private var mContext: Context? = null

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        mContext = context
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        requestQueue = VolleySingletonClass.getInstance(mContext!!)!!

        setMemberListData(view)
        return view
    }

    private fun setMemberListData(view: View) {
        val database = FirebaseDatabase.getInstance().reference.child(AppConstant.MEMBERS)
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot?) {
                if (dataSnapshot?.hasChild(AppConstant.MEMBERS_LIST)!!) {
                    val memberNodeList = dataSnapshot.child(AppConstant.MEMBERS_LIST)?.value as ArrayList<*>
                    memberList = ModelInfoUtils.getMemberListModel(memberNodeList)
                    memberListAdapter = MemberListAdapter(mContext!!, memberList!!, this@HomeFragment)

                    view.recyclerView.layoutManager = LinearLayoutManager(mContext, LinearLayout.VERTICAL, false)
                    view.recyclerView.adapter = memberListAdapter
                }
            }

            override fun onCancelled(dataSnapshot: DatabaseError?) {
            }

        })
    }

    override fun onViewClick(position: Int) {
        val memberShortInfoModel = memberList?.get(position)
        sendFriendRequest("4041",memberShortInfoModel!!.memberId,"message1",memberShortInfoModel.registrationToken)
    }

    private fun sendFriendRequest(senderId: String, ReceiverId: String,message:String,registrationToken: String) {
        val request = getJsonBody(senderId, ReceiverId,message,registrationToken)
        val jsonRequest = object : JsonObjectRequest(Request.Method.POST, AppConstant.SEND_NOTIFICATION_URL, request,
                Response.Listener<JSONObject> { response: JSONObject? ->
                    val success= response!!.getInt("success")
                },
                Response.ErrorListener { error: VolleyError? ->

                }
        ) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val params = java.util.HashMap<String, String>()
                params.put("Content-Type", "application/json")
                params.put("Authorization", AppConstant.Authorization)

                return params
            }
        }
        requestQueue.add(jsonRequest)
    }

    private fun getJsonBody(senderId: String, ReceiverId: String,message:String,registrationToken:String): JSONObject {
        val jsonObjectRequestParams = JSONObject()

        val jsonObjectData = JSONObject()
        jsonObjectData.put("type",1)
        jsonObjectData.put(AppConstant.SENDER_ID_KEY, senderId)
        jsonObjectData.put(AppConstant.RECEIVER_ID_KEY, ReceiverId)
        jsonObjectData.put("message", message)
        jsonObjectRequestParams.put("friendRequestData", jsonObjectData)
        jsonObjectRequestParams.put("to", SharedPreferencesUtils.getRegistrationKey(mContext!!))

        return jsonObjectRequestParams
    }
}
