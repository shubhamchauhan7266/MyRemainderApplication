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
import com.myremainderapplication.models.MemberFriendInfoModel
import com.myremainderapplication.models.MemberShortInfoModel
import com.myremainderapplication.utils.ModelInfoUtils
import kotlinx.android.synthetic.main.fragment_friend_list.view.*

/**
 * <h1><font color="orange">FriendListFragment</font></h1>
 * It is a Fregment which is used to display a list of Friends
 *
 * @author Shubham Chauhan
 */
class FriendListFragment : Fragment(), FriendListAdapter.IFriendListAdapterCallBack {
    private var friendList: ArrayList<MemberFriendInfoModel>? = null
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
        val database = FirebaseDatabase.getInstance().reference.child(AppConstant.MEMBERS).child("4041")
        database.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot?) {
                if(dataSnapshot?.hasChild(AppConstant.FRIEND_LIST)!!){
                    val memberList = dataSnapshot.child(AppConstant.FRIEND_LIST)?.value as ArrayList<*>
                    val tempFriendList = ModelInfoUtils.Companion.getFriendList(memberList)
                    friendList= ArrayList()

                    for (friendInfo:MemberFriendInfoModel in tempFriendList){
                        if(friendInfo.friendStatus==ModelInfoUtils.FRIEND){
                            friendList!!.add(friendInfo)
                        }
                    }

                    friendListAdapter = FriendListAdapter(this@FriendListFragment, friendList!!)
                    view.recyclerView.layoutManager = LinearLayoutManager(mContext, LinearLayout.VERTICAL, false)
                    view.recyclerView.adapter = friendListAdapter
                }
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
