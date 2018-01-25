package com.myremainderapplication.adapters

import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.myremainderapplication.R
import com.myremainderapplication.fragments.NotificationFragment
import com.myremainderapplication.interfaces.AppConstant
import com.myremainderapplication.models.MemberNotificationModel
import com.myremainderapplication.services.HandleFriendRequestService
import kotlinx.android.synthetic.main.customnotification.view.*
import kotlinx.android.synthetic.main.notification_item_row.view.*

/**
 * <h1><font color="orange">NotificationListAdapter</font></h1>
 * It is a Adapter class which is used for NotificationFragment
 *
 * @author Shubham Chauhan
 */
class NotificationListAdapter(val context: NotificationFragment, private var notificationList: ArrayList<MemberNotificationModel>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == AppConstant.FRIEND_REQUEST_TYPE) {
            val view = LayoutInflater.from(parent!!.context).inflate(R.layout.friend_request_notification_item_row, parent, false)
            return ViewHolderFriendRequest(view)
        } else if (viewType == AppConstant.EVENT_ALERT_TYPE) {
            val view = LayoutInflater.from(parent!!.context).inflate(R.layout.notification_item_row, parent, false)
            return ViewHolderEventAlert(view)
        } else {
            val view = LayoutInflater.from(parent!!.context).inflate(R.layout.notification_item_row, parent, false)
            return ViewHolderEventAlert(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        if (holder is ViewHolderEventAlert) {
            val viewHolderEventAler: ViewHolderEventAlert = holder
            viewHolderEventAler.bindEventData(notificationList[position])
        } else if (holder is ViewHolderFriendRequest) {
            val viewHolderFriendRequest: ViewHolderFriendRequest = holder
            viewHolderFriendRequest.bindFriendRequestData(context, notificationList[position])
        }
    }

    override fun getItemCount(): Int {
        return notificationList.size
    }

    override fun getItemViewType(position: Int): Int {
        if (notificationList[position].type == AppConstant.FRIEND_REQUEST_TYPE) {
            return AppConstant.FRIEND_REQUEST_TYPE
        } else if (notificationList[position].type == AppConstant.EVENT_ALERT_TYPE) {
            return AppConstant.EVENT_ALERT_TYPE
        } else {
            return AppConstant.SIMPE_ALERT_TYPE
        }
    }

    /**
     * Create View holder for Event Alert
     */
    class ViewHolderEventAlert(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindEventData(memberNotificationModel: MemberNotificationModel) {
            itemView.tvTitle.text = memberNotificationModel.title
            itemView.tvDescription.text = memberNotificationModel.body
        }

    }

    /**
     * Create View holder for Friend Request
     */
    class ViewHolderFriendRequest(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        fun bindFriendRequestData(context: NotificationFragment, memberNotificationModel: MemberNotificationModel) {
            itemView.tvMessage.text = memberNotificationModel.body

            itemView.tvAccept.setOnClickListener {
                val acceptIntent = Intent(context.activity, HandleFriendRequestService::class.java)
                acceptIntent.action = context.getString(R.string.accept)
                acceptIntent.putExtra(AppConstant.SENDER_ID_KEY, memberNotificationModel.senderId)
                acceptIntent.putExtra(AppConstant.RECEIVER_ID_KEY, memberNotificationModel.receiverId)
                context.activity?.startService(acceptIntent)
            }

            itemView.tvReject.setOnClickListener {
                val rejectIntent = Intent(context.activity, HandleFriendRequestService::class.java)
                rejectIntent.action = context.getString(R.string.reject)
                rejectIntent.putExtra(AppConstant.SENDER_ID_KEY, memberNotificationModel.senderId)
                rejectIntent.putExtra(AppConstant.RECEIVER_ID_KEY, memberNotificationModel.receiverId)
                context.activity?.startService(rejectIntent)
            }
        }

    }
}