package com.myremainderapplication.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.myremainderapplication.R
import com.myremainderapplication.fragments.FriendListFragment
import com.myremainderapplication.interfaces.AppConstant
import com.myremainderapplication.models.MemberFriendInfoModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.friend_item_row.view.*

/**
 * <h1><font color="orange">FriendListAdapter</font></h1>
 * It is a Adapter class which is used for FriendListFragment
 *
 * @author Shubham Chauhan
 */
class FriendListAdapter(private val context:Context,private val iFriendListAdapterCallBack: IFriendListAdapterCallBack, private var friendList:ArrayList<MemberFriendInfoModel>,private val viewType:Int) : RecyclerView.Adapter<FriendListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int):FriendListAdapter.ViewHolder{
        if(viewType==AppConstant.VIEW_HORIZONTAL){
            val view= LayoutInflater.from(parent!!.context).inflate(R.layout.friend_item_row, parent, false)
            return ViewHolder(view)
        }else{
            val view= LayoutInflater.from(parent!!.context).inflate(R.layout.friend_item_row, parent, false)
            return ViewHolder(view)
        }
    }

    override fun getItemCount(): Int {
        return friendList.size
    }

    override fun getItemViewType(position: Int): Int {
        return if(viewType==AppConstant.VIEW_HORIZONTAL)
            AppConstant.VIEW_HORIZONTAL
        else
            AppConstant.VIEW_VERTICAL
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int)= holder!!.bind(context,friendList[position],position,iFriendListAdapterCallBack)

    class ViewHolder(itemView:View) :RecyclerView.ViewHolder(itemView){
        fun bind(context:Context,memberShortInfoModel: MemberFriendInfoModel, position: Int, iFriendListAdapterCallBack: IFriendListAdapterCallBack?) {
            itemView.tvName.text= memberShortInfoModel.memberName
            Picasso.with(context)
                    .load(memberShortInfoModel.imagePath)
                    .resize(100, 100)
                    .into(itemView.ivProfile)
            itemView.friendItemView.setOnClickListener {
                iFriendListAdapterCallBack!!.onFriendViewClick(position)
            }
        }
    }

    interface IFriendListAdapterCallBack{
        fun onFriendViewClick(position:Int)
    }
}