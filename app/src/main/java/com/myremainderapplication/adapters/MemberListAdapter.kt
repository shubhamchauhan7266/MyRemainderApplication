package com.myremainderapplication.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import com.myremainderapplication.models.MemberShortInfoModel
import android.view.View
import android.view.ViewGroup
import com.myremainderapplication.R
import com.myremainderapplication.models.MemberFriendInfoModel
import com.myremainderapplication.utils.ModelInfoUtils
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.member_item_row.view.*

/**
 * <h1><font color="orange">MemberListAdapter</font></h1>
 * It is a Adapter class which is used for HomeFragment
 *
 * @author Shubham Chauhan
 */
class MemberListAdapter(val context: Context, private var memberList: ArrayList<MemberShortInfoModel>, private var friendList: ArrayList<MemberFriendInfoModel>,
                        val iMemberListAdapterCallBack: IMemberListAdapterCallBack) : RecyclerView.Adapter<MemberListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.member_item_row, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(context, memberList[position],
            position, friendList, iMemberListAdapterCallBack)


    override fun getItemCount(): Int {
        return memberList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(context: Context, memberShortInfoModel: MemberShortInfoModel, position: Int, friendList: ArrayList<MemberFriendInfoModel>,
                 iMemberListAdapterCallBack: IMemberListAdapterCallBack) {
            val status = getStatusOfFriend(memberShortInfoModel, friendList)

            if (status == ModelInfoUtils.FRIEND_REQUEST_SENT) {
                itemView.btAddFriend.text = context.getString(R.string.friend_request_sent)
            } else if (status == ModelInfoUtils.FRIEND) {
                itemView.btAddFriend.text = context.getString(R.string.friend)
            } else {
                itemView.btAddFriend.setOnClickListener {
                    itemView.btAddFriend.text = context.getString(R.string.add_friend)
                    iMemberListAdapterCallBack.onMemberViewClick(position)
                }
            }

            itemView.tvName.text = memberShortInfoModel.memberName
            Picasso.with(context)
                    .load(memberShortInfoModel.imagePath)
                    .resize(100, 100)
                    .into(itemView.ivProfile)
        }

        /**
         * Method is used to get status of friend
         */
        private fun getStatusOfFriend(memberShortInfoModel: MemberShortInfoModel,
                                      friendList: ArrayList<MemberFriendInfoModel>): Int {
            for (memberFriendInfoModel: MemberFriendInfoModel in friendList) {
                if (memberShortInfoModel.memberId == memberFriendInfoModel.memberId) {
                    return memberFriendInfoModel.friendStatus
                }
            }
            return -1
        }
    }

    interface IMemberListAdapterCallBack {
        fun onMemberViewClick(position: Int)
    }
}