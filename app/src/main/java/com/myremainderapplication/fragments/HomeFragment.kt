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
import com.myremainderapplication.models.MemberFriendInfoModel
import com.myremainderapplication.models.MemberShortInfoModel
import com.myremainderapplication.utils.ModelInfoUtils
import com.myremainderapplication.utils.SharedPreferencesUtils
import com.myremainderapplication.utils.VolleySingletonClass
import kotlinx.android.synthetic.main.fragment_home.view.*
import org.json.JSONObject


/**
 * <h1><font color="orange">HomeFragment</font></h1>
 * It is a Fregment which is used to display a list of Members
 *
 * @author Shubham Chauhan
 */
class HomeFragment : Fragment(), MemberListAdapter.IMemberListAdapterCallBack {

    private var memberList: ArrayList<MemberShortInfoModel>? = null
    private var friendList: ArrayList<MemberFriendInfoModel>? = null
    private lateinit var memberListAdapter: MemberListAdapter
    private lateinit var requestQueue: RequestQueue
    private var mContext: Context? = null
    private lateinit var currentFriendId: String
    private lateinit var memberId: String

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        mContext = context
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        memberId = SharedPreferencesUtils.getMemberId(mContext!!).toString()
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        requestQueue = VolleySingletonClass.getInstance(mContext!!)!!
        friendList = ArrayList()

        setMemberListData(view)
        return view
    }

    private fun setMemberListData(view: View) {
        var isUpdateRequired = false
        val database = FirebaseDatabase.getInstance().reference.child(AppConstant.MEMBERS)
        database.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot?) {
                val memberNodeList = dataSnapshot?.child(AppConstant.MEMBERS_LIST)?.value as ArrayList<*>
                val tempMemberList = ModelInfoUtils.getMemberList(memberNodeList)
                currentFriendId = dataSnapshot.child(memberId).child(AppConstant.CURRENT_FRIEND_LIST_ID).value as String
                memberList = ArrayList()

                if (!isUpdateRequired) {
                    isUpdateRequired = true
                    updateNewRegistrationToken(dataSnapshot)
                }

                if (dataSnapshot.child(memberId).hasChild(AppConstant.FRIEND_LIST)) {
                    val friendDataList = dataSnapshot.child(memberId).child(AppConstant.FRIEND_LIST)?.value as ArrayList<*>
                    friendList = ModelInfoUtils.getFriendList(friendDataList)
                }

                for (memberInfo: MemberShortInfoModel in tempMemberList) {
                    if (memberInfo.memberId != memberId) {
                        memberList!!.add(memberInfo)
                    }
                }

                memberListAdapter = MemberListAdapter(mContext!!, memberList!!, friendList!!, this@HomeFragment)
                view.recyclerView.layoutManager = LinearLayoutManager(mContext, LinearLayout.VERTICAL, false)
                view.recyclerView.adapter = memberListAdapter
            }

            override fun onCancelled(dataSnapshot: DatabaseError?) {
            }

        })
    }


    override fun onViewClick(position: Int) {
        val memberShortInfoModel = memberList?.get(position)
        sendFriendRequest(memberId, memberShortInfoModel!!.memberId, "message1",
                memberShortInfoModel.registrationToken, position)
    }

    private fun sendFriendRequest(senderId: String, ReceiverId: String, message: String, registrationToken: String, position: Int) {
        val request = getJsonBody(senderId, ReceiverId, message, registrationToken)

        val jsonRequest = object : JsonObjectRequest(Request.Method.POST, AppConstant.SEND_NOTIFICATION_URL, request,
                Response.Listener<JSONObject> { response: JSONObject? ->
                    val success = response!!.getInt("success")
                    if (success != 0) {
                        updateSenderFriendList(position)
                    }
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

    private fun getJsonBody(senderId: String, ReceiverId: String, message: String, registrationToken: String): JSONObject {
        val jsonObjectRequestParams = JSONObject()

        val jsonObjectData = JSONObject()
        jsonObjectData.put("type", AppConstant.FRIEND_REQUEST_TYPE)
        jsonObjectData.put(AppConstant.SENDER_ID_KEY, senderId)
        jsonObjectData.put(AppConstant.RECEIVER_ID_KEY, ReceiverId)
        jsonObjectData.put("message", message)

        jsonObjectRequestParams.put("data", jsonObjectData)
        jsonObjectRequestParams.put("to", registrationToken)

        return jsonObjectRequestParams
    }

    private fun updateSenderFriendList(position: Int) {
        val receiverInfo = memberList?.get(position)
        val databaseSenderRef = FirebaseDatabase.getInstance().reference.child(AppConstant.MEMBERS).child(memberId)
        val newSenderFriendId = (currentFriendId.toInt() + 1).toString()
        ModelInfoUtils.addFriend(databaseSenderRef, newSenderFriendId, receiverInfo!!, ModelInfoUtils.FRIEND_REQUEST_SENT)
    }

    private fun updateNewRegistrationToken(dataSnapshot: DataSnapshot) {
        val memberList = dataSnapshot.child(AppConstant.MEMBERS_LIST)?.value as ArrayList<*>
        val memberUserList = ModelInfoUtils.Companion.getMemberList(memberList)

        var index = 0
        while (index < memberUserList.size) {
            if (memberId == memberUserList[index].memberId) {
                ModelInfoUtils.updateRegistrationToken(mContext!!, memberId, index.toString())
                break
            }
            index++
        }
    }
}
