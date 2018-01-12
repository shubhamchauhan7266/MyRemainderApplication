package com.myremainderapplication.fragments

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.google.firebase.database.*

import com.myremainderapplication.R
import com.myremainderapplication.adapters.MemberListAdapter
import com.myremainderapplication.interfaces.AppConstant
import com.myremainderapplication.models.MemberShortInfoModel
import com.myremainderapplication.utils.ModelInfoUtils
import kotlinx.android.synthetic.main.fragment_home.view.*


/**
 * A simple [Fragment] subclass.
 */
class HomeFragment : Fragment(), MemberListAdapter.IMemberListAdapterCallBack {

    private var memberList: ArrayList<MemberShortInfoModel>? = null
    private lateinit var memberListAdapter: MemberListAdapter
    private var mContext: Context? = null
    private var currentFriendId: String = ""

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        mContext = context
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        setMemberListData(view)
        return view
    }

    private fun setMemberListData(view: View) {
        val database = FirebaseDatabase.getInstance().reference.child(AppConstant.MEMBERS)
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot?) {
                if (dataSnapshot?.hasChild(AppConstant.MEMBERS_LIST)!!) {
                    currentFriendId = dataSnapshot.child("4041").child(AppConstant.CURRENT_FRIEND_LIST_ID).value.toString()
                    val memberNodeList = dataSnapshot.child(AppConstant.MEMBERS_LIST)?.value as ArrayList<*>
                    memberList = ModelInfoUtils.getMemberListModel(memberNodeList)
                    memberListAdapter = MemberListAdapter(mContext!!, memberList!!, this@HomeFragment)

                    view.recyclerView.layoutManager = LinearLayoutManager(mContext, LinearLayout.VERTICAL, false)
                    view.recyclerView.adapter = memberListAdapter
                }
            }

            override fun onCancelled(dataSnapshot: DatabaseError?) {
            }

        })
    }

    override fun onViewClick(position: Int) {

        val memberShortInfoModel = memberList?.get(position)
        val databaseMemberRef: DatabaseReference = FirebaseDatabase.getInstance().reference.child(AppConstant.MEMBERS).child("4041")
        val newFriendId = (currentFriendId.toInt() + 1).toString()

        val hasMapMemberListUserNode = HashMap<String, String>()
        hasMapMemberListUserNode.put(AppConstant.MEMBER_ID, memberShortInfoModel!!.memberId)
        hasMapMemberListUserNode.put(AppConstant.MEMBER_NAME, memberShortInfoModel.memberName)
        hasMapMemberListUserNode.put(AppConstant.IMAGE_PATH, memberShortInfoModel.imagePath)
        hasMapMemberListUserNode.put(AppConstant.REGISTRATION_TOKEN, memberShortInfoModel.registrationToken)

        val hasMapMemberListNode = HashMap<String, HashMap<String, String>>()
        hasMapMemberListNode.put(newFriendId, hasMapMemberListUserNode)
        databaseMemberRef.child(AppConstant.FRIEND_LIST).updateChildren(hasMapMemberListNode as Map<String, Any>?)

        val hasMapFriendId = HashMap<String, String>()
        hasMapFriendId.put(AppConstant.CURRENT_FRIEND_LIST_ID, newFriendId)
        databaseMemberRef.updateChildren(hasMapFriendId as Map<String, Any>?)
    }
}
