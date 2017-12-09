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
import com.myremainderapplication.adapters.FriendListAdapter
import com.myremainderapplication.interfaces.AppConstant
import com.myremainderapplication.models.MemberIdNameModel
import kotlinx.android.synthetic.main.fragment_friend_list.view.*

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [FriendListFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [FriendListFragment.newInstance] factory method to
 * create an instance of this fragment.
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

        friendList = ArrayList()
        friendListAdapter = FriendListAdapter(this, friendList!!)

        view.recyclerView.layoutManager = LinearLayoutManager(mContext, LinearLayout.VERTICAL, false)
        view.recyclerView.adapter = friendListAdapter

        setFriendListData()
        return view
    }

    private fun setFriendListData() {
        val database = FirebaseDatabase.getInstance().reference
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot?) {
                val memberList = dataSnapshot?.child(AppConstant.MEMBERS)?.child(AppConstant.MEMBERS_LIST)?.value as ArrayList<*>
                val size = memberList.size

                var index = 0
                while (index < size) {
                    val hashMap = memberList.get(index) as HashMap<String, String>
                    val id = hashMap.get(AppConstant.MEMBER_ID).toString()
                    val name = hashMap.get(AppConstant.MEMBER_NAME)
                    friendList!!.add(MemberIdNameModel(id, name!!))
                    index++
                }
                friendList?.let { friendListAdapter.setFriendList(it) }
                friendListAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(dataSnapshot: DatabaseError?) {
            }

        })
    }

    override fun onViewClick(position: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
