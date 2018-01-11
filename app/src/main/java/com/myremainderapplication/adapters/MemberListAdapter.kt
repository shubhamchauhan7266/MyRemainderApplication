package com.myremainderapplication.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import com.myremainderapplication.models.MemberShortInfoModel
import android.view.View
import android.view.ViewGroup
import com.myremainderapplication.R
import com.myremainderapplication.fragments.HomeFragment
import kotlinx.android.synthetic.main.member_item_row.view.*

/**
 * Created by user on 11/1/18.
 */
class MemberListAdapter(val context: HomeFragment, private var memberList: ArrayList<MemberShortInfoModel>) : RecyclerView.Adapter<MemberListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent!!.context).inflate(R.layout.member_item_row, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) = holder!!.bind(memberList[position])


    override fun getItemCount(): Int {
        return memberList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(memberShortInfoModel: MemberShortInfoModel) {
            itemView.tvName.text = memberShortInfoModel.memberName
        }
    }
}