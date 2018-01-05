package com.myremainderapplication.fragments

import android.content.Context
import android.content.Intent
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
import com.myremainderapplication.activities.ProfileActivity
import com.myremainderapplication.adapters.FriendListAdapter
import com.myremainderapplication.interfaces.AppConstant
import com.myremainderapplication.models.MemberIdNameModel
import com.myremainderapplication.utils.ModelInfoUtils
import kotlinx.android.synthetic.main.fragment_friend_list.view.*

/**
 * A simple [Fragment] subclass.
 */
class FriendListFragment : Fragment(), FriendListAdapter.IFriendListAdapterCallBack {
    private var friendList: ArrayList<MemberIdNameModel>? = null
    private lateinit var friendListAdapter: FriendListAdapter
    private var mContext: Context? = null

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        mContext = context
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_friend_list, container, false)

        setFriendListData(view)
        return view
    }

    private fun setFriendListData(view: View) {
        val database = FirebaseDatabase.getInstance().reference
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot?) {
                val memberList = dataSnapshot?.child(AppConstant.MEMBERS)?.child(AppConstant.MEMBERS_LIST)?.value as ArrayList<*>
                friendList = ModelInfoUtils.Companion.getMemberIdNameModel(memberList)
                friendListAdapter = FriendListAdapter(this@FriendListFragment, friendList!!)

                view.recyclerView.layoutManager = LinearLayoutManager(mContext, LinearLayout.VERTICAL, false)
                view.recyclerView.adapter = friendListAdapter
            }

            override fun onCancelled(dataSnapshot: DatabaseError?) {
            }

        })
    }

    override fun onViewClick(position: Int) {
        val memberIdNameModel = friendList!![position]
        val intent = Intent(mContext, ProfileActivity::class.java)
        intent.putExtra(AppConstant.MEMBER_ID, memberIdNameModel.memberId)
        startActivity(intent)
    }
}
