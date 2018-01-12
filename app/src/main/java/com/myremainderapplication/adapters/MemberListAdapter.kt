package com.myremainderapplication.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import com.myremainderapplication.models.MemberShortInfoModel
import android.view.View
import android.view.ViewGroup
import com.myremainderapplication.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.member_item_row.view.*

/**
 * Created by user on 11/1/18.
 */
class MemberListAdapter(val context: Context, private var memberList: ArrayList<MemberShortInfoModel>, val iMemberListAdapterCallBack: IMemberListAdapterCallBack) : RecyclerView.Adapter<MemberListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent!!.context).inflate(R.layout.member_item_row, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) = holder!!.bind(context, memberList[position], position, iMemberListAdapterCallBack)


    override fun getItemCount(): Int {
        return memberList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(context: Context, memberShortInfoModel: MemberShortInfoModel, position: Int, iMemberListAdapterCallBack: IMemberListAdapterCallBack) {
            itemView.tvName.text = memberShortInfoModel.memberName
            itemView.btAddFriend.setOnClickListener { iMemberListAdapterCallBack.onViewClick(position) }
            Picasso.with(context)
                    .load(memberShortInfoModel.imagePath)
                    .resize(100, 100)
                    .into(itemView.ivProfile)
        }
    }

    interface IMemberListAdapterCallBack {
        fun onViewClick(position: Int)
    }
}