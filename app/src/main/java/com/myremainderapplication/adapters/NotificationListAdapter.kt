package com.myremainderapplication.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.myremainderapplication.R
import com.myremainderapplication.fragments.NotificationFragment
import com.myremainderapplication.models.MemberNotificationModel
import kotlinx.android.synthetic.main.notification_item_row.view.*

/**
 * <h1><font color="orange">NotificationListAdapter</font></h1>
 * It is a Adapter class which is used for NotificationFragment
 *
 * @author Shubham Chauhan
 */
class NotificationListAdapter(val context: NotificationFragment, private var notificationList: ArrayList<MemberNotificationModel>) : RecyclerView.Adapter<NotificationListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent!!.context).inflate(R.layout.notification_item_row, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int)=holder!!.bind(notificationList[position])

    override fun getItemCount(): Int {
        return notificationList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(memberNotificationModel: MemberNotificationModel) {
            itemView.tvTitle.text=memberNotificationModel.title
            itemView.tvDescription.text=memberNotificationModel.body
        }

    }
}