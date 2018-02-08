package com.myremainderapplication.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.myremainderapplication.R
import com.myremainderapplication.fragments.FriendListFragment
import com.myremainderapplication.models.MemberFriendInfoModel
import kotlinx.android.synthetic.main.friend_item_row.view.*

/**
 * <h1><font color="orange">FriendListAdapter</font></h1>
 * It is a Adapter class which is used for FriendListFragment
 *
 * @author Shubham Chauhan
 */
class FriendListAdapter(private val iFriendListAdapterCallBack: IFriendListAdapterCallBack, private var friendList:ArrayList<MemberFriendInfoModel>) : RecyclerView.Adapter<FriendListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int):FriendListAdapter.ViewHolder{
        val view= LayoutInflater.from(parent!!.context).inflate(R.layout.friend_item_row, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return friendList.size
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int)= holder!!.bind(friendList[position],position,iFriendListAdapterCallBack)

    class ViewHolder(itemView:View) :RecyclerView.ViewHolder(itemView){
        fun bind(memberShortInfoModel: MemberFriendInfoModel, position: Int, iFriendListAdapterCallBack: IFriendListAdapterCallBack?) {
            itemView.tvName.text= memberShortInfoModel.memberName
            itemView.friendItemView.setOnClickListener {
                iFriendListAdapterCallBack!!.onFriendViewClick(position)
            }
        }
    }

    interface IFriendListAdapterCallBack{
        fun onFriendViewClick(position:Int)
    }
}