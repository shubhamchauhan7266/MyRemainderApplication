package com.myremainderapplication.fragments

import android.content.Context
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
import com.myremainderapplication.adapters.MemberListAdapter
import com.myremainderapplication.interfaces.AppConstant
import com.myremainderapplication.models.MemberShortInfoModel
import com.myremainderapplication.utils.ModelInfoUtils
import kotlinx.android.synthetic.main.fragment_home.view.*


/**
 * A simple [Fragment] subclass.
 */
class HomeFragment : Fragment() {
    private var memberList: ArrayList<MemberShortInfoModel>? = null
    private lateinit var memberListAdapter: MemberListAdapter
    private var mContext: Context? = null

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
                    val memberNodeList = dataSnapshot.child(AppConstant.MEMBERS_LIST)?.value as ArrayList<*>
                    memberList = ModelInfoUtils.getMemberListModel(memberNodeList)
                    memberListAdapter = MemberListAdapter(mContext!!, memberList!!)

                    view.recyclerView.layoutManager = LinearLayoutManager(mContext, LinearLayout.VERTICAL, false)
                    view.recyclerView.adapter = memberListAdapter
                }
            }

            override fun onCancelled(dataSnapshot: DatabaseError?) {
            }

        })
    }

}
