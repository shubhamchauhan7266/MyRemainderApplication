package com.myremainderapplication.fragments

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

import com.myremainderapplication.R
import com.myremainderapplication.adapters.NotificationListAdapter
import com.myremainderapplication.interfaces.AppConstant
import com.myremainderapplication.models.MemberNotificationModel
import com.myremainderapplication.utils.ModelInfoUtils
import kotlinx.android.synthetic.main.fragment_notifaction.view.*

/**
 * <h1><font color="orange">NotificationFragment</font></h1>
 * It is a Fregment which is used to display a list of Notification
 *
 * @author Shubham Chauhan
 */
class NotificationFragment : Fragment() {
    private var mContext: Context? = null
    private var notificationList: ArrayList<MemberNotificationModel>? = null
    private lateinit var notificationListAdapter: NotificationListAdapter

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        mContext = context
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_notifaction, container, false)
        setNotificationListData(view)
        return view
    }

    private fun setNotificationListData(view: View) {
        val database = FirebaseDatabase.getInstance().reference.child(AppConstant.MEMBERS).child("4041")
        database.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot?) {
                if (dataSnapshot?.hasChild(AppConstant.NOTIFICATION_LIST)!!) {
                    val memberNotificationList = dataSnapshot.child(AppConstant.NOTIFICATION_LIST)?.value as ArrayList<*>
                    notificationList = ModelInfoUtils.Companion.getNotificationList(memberNotificationList)
                    notificationListAdapter = NotificationListAdapter(this@NotificationFragment, notificationList!!)

                    view.recyclerView.layoutManager = LinearLayoutManager(mContext, LinearLayout.VERTICAL, false)
                    view.recyclerView.adapter = notificationListAdapter
                }
            }

            override fun onCancelled(dataSnapshot: DatabaseError?) {
            }

        })
    }
}
