package com.myremainderapplication.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.myremainderapplication.R
import com.myremainderapplication.fragments.FriendListFragment
import com.myremainderapplication.models.MemberIdNameModel
import kotlinx.android.synthetic.main.friend_item_row.view.*

/**
 * Created by user on 8/12/17.
 */
class FriendListAdapter(val context: FriendListFragment, val friendList:ArrayList<MemberIdNameModel>) : RecyclerView.Adapter<FriendListAdapter.ViewHolder>() {
    private val iFriendListAdapterCallBack: IFriendListAdapterCallBack? = context

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int):FriendListAdapter.ViewHolder{
        val view= LayoutInflater.from(parent!!.context).inflate(R.layout.friend_item_row, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return friendList.size
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int)= holder!!.bind(friendList[position],position,iFriendListAdapterCallBack)

    class ViewHolder(itemView:View) :RecyclerView.ViewHolder(itemView){
        fun bind(memberIdNameModel: MemberIdNameModel, position: Int, iFriendListAdapterCallBack: IFriendListAdapterCallBack?) {
            itemView.tvName.text=memberIdNameModel.memberName
            itemView.friendItemView.setOnClickListener {
                iFriendListAdapterCallBack!!.onViewClick(position)
            }
        }
    }

    interface IFriendListAdapterCallBack{
        fun onViewClick(position:Int)
    }
}