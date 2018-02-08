package com.myremainderapplication.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.*
import android.widget.LinearLayout
import com.android.volley.*
import com.android.volley.toolbox.JsonObjectRequest
import com.google.firebase.database.*

import com.myremainderapplication.R
import com.myremainderapplication.activities.ProfileActivity
import com.myremainderapplication.adapters.FriendListAdapter
import com.myremainderapplication.adapters.MemberListAdapter
import com.myremainderapplication.adapters.NotificationListAdapter
import com.myremainderapplication.interfaces.AppConstant
import com.myremainderapplication.models.MemberFriendInfoModel
import com.myremainderapplication.models.MemberNotificationModel
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
class HomeFragment : Fragment(), MemberListAdapter.IMemberListAdapterCallBack, FriendListAdapter.IFriendListAdapterCallBack {

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
        memberList = ArrayList()
        friendList = ArrayList()

        setMemberListData(view)
        return view
    }

    /**
     * Method is used to get memberList from database and then set in adapter for display
     * @param view
     */
    private fun setMemberListData(view: View) {
        val database = FirebaseDatabase.getInstance().reference.child(AppConstant.MEMBERS)
        database.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot?) {
                val memberNodeList = dataSnapshot?.child(AppConstant.MEMBERS_LIST)?.value as ArrayList<*>
                val tempMemberList = ModelInfoUtils.getMemberList(memberNodeList)
                currentFriendId = dataSnapshot.child(memberId).child(AppConstant.CURRENT_FRIEND_LIST_ID).value as String
                memberList?.clear()


                // for FriendList
                if (dataSnapshot.child(memberId).hasChild(AppConstant.FRIEND_LIST)) {
                    val friendDataList = dataSnapshot.child(memberId).child(AppConstant.FRIEND_LIST)?.value as ArrayList<*>
                    friendList = ModelInfoUtils.getFriendList(friendDataList)
                }
                val friendListAdapter = FriendListAdapter(mContext!!, this@HomeFragment, friendList!!, AppConstant.VIEW_HORIZONTAL)
                view.recyclerViewFriend.layoutManager = LinearLayoutManager(mContext, LinearLayout.HORIZONTAL, false)
                view.recyclerViewFriend.adapter = friendListAdapter

                // for NotificationList
                val notificationList = ArrayList<MemberNotificationModel>()
                if (dataSnapshot.child(memberId).hasChild(AppConstant.NOTIFICATION_LIST)) {
                    val notificationDataList = dataSnapshot.child(memberId).child(AppConstant.NOTIFICATION_LIST)?.value as ArrayList<*>
                    val tempNotificationList = ModelInfoUtils.getNotificationList(notificationDataList)
                    tempNotificationList
                            .filter { it.type == AppConstant.SIMPE_ALERT_TYPE }
                            .forEach { notificationList.add(it) }
                }
                val notificationListAdapter = NotificationListAdapter(mContext!!, notificationList, AppConstant.VIEW_HORIZONTAL)
                view.recyclerViewFriendRequest.layoutManager = LinearLayoutManager(mContext, LinearLayout.VERTICAL, false)
                view.recyclerViewFriendRequest.adapter = notificationListAdapter


                // for MemberList
                tempMemberList
                        .filter { it.memberId != memberId }
                        .forEach { memberList!!.add(it) }

                memberListAdapter = MemberListAdapter(mContext!!, memberList!!, friendList!!, this@HomeFragment)
                view.recyclerViewMemberList.layoutManager = LinearLayoutManager(mContext, LinearLayout.VERTICAL, false)
                view.recyclerViewMemberList.adapter = memberListAdapter
            }

            override fun onCancelled(dataSnapshot: DatabaseError?) {
            }

        })
    }


    override fun onMemberViewClick(position: Int) {
        val memberShortInfoModel = memberList?.get(position)
        sendFriendRequest(memberId, memberShortInfoModel!!.memberId, "message1",
                memberShortInfoModel.registrationToken, position)
    }

    override fun onFriendViewClick(position: Int) {
        val memberIdNameModel = friendList!![position]
        val intent = Intent(mContext, ProfileActivity::class.java)
        intent.putExtra(AppConstant.MEMBER_ID, memberIdNameModel.memberId)
        startActivity(intent)
    }

    /**
     * Method is used to send friend request via Notification
     * @param senderId
     * @param ReceiverId
     * @param message
     * @param registrationToken
     * @param position
     */
    private fun sendFriendRequest(senderId: String, ReceiverId: String, message: String, registrationToken: String, position: Int) {
        val request = getJsonBody(senderId, ReceiverId, message, registrationToken)

        val jsonRequest = object : JsonObjectRequest(Request.Method.POST, AppConstant.SEND_NOTIFICATION_URL, request,
                Response.Listener<JSONObject> { response: JSONObject? ->
                    val success = response!!.getInt("success")
                    if (success != 0) {
                        updateSenderFriendList(position)
                    }
                },
                Response.ErrorListener {
                    //error: VolleyError? ->

                }
        ) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val params = java.util.HashMap<String, String>()
                params["Content-Type"] = "application/json"
                params["Authorization"] = AppConstant.Authorization

                return params
            }
        }
        requestQueue.add(jsonRequest)
    }

    /**
     * Method is used to get json payload for notification(friend request) API
     * @param senderId
     * @param ReceiverId
     * @param message
     * @param registrationToken
     * @return JSONObject
     */
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

    /**
     * Method is used to add friend in sender friend list
     * @param position
     */
    private fun updateSenderFriendList(position: Int) {
        val receiverInfo = memberList?.get(position)
        val databaseSenderRef = FirebaseDatabase.getInstance().reference.child(AppConstant.MEMBERS).child(memberId)
        val newSenderFriendId = (currentFriendId.toInt() + 1).toString()
        ModelInfoUtils.addFriend(databaseSenderRef, newSenderFriendId, receiverInfo!!, ModelInfoUtils.FRIEND_REQUEST_SENT)
    }
}
