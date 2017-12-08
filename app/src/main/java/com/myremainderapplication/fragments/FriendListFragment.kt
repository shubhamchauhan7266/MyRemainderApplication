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
import kotlinx.android.synthetic.main.fragment_friend_list.*

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [FriendListFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [FriendListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FriendListFragment : Fragment(),FriendListAdapter.IFriendListAdapterCallBack {
    private var friendList: ArrayList<MemberIdNameModel>? = null
    private lateinit var friendListAdapter: FriendListAdapter
    private var mContext: Context? = null

    // TODO: Rename and change types of parameters
    private var mParam1: String? = null
    private var mParam2: String? = null

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        mContext=context
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
//            mParam1 = arguments!!.getString(ARG_PARAM1)
//            mParam2 = arguments!!.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view= inflater.inflate(R.layout.fragment_friend_list, container, false)

        friendList= ArrayList()
        friendListAdapter=FriendListAdapter(this, friendList!!)
        recyclerView.layoutManager=LinearLayoutManager(mContext, LinearLayout.VERTICAL, false)
        recyclerView.adapter=friendListAdapter

        setFriendListData()
        return view
    }

    private fun setFriendListData() {
        val database = FirebaseDatabase.getInstance().reference
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot?) {
                friendList = dataSnapshot?.child(AppConstant.MEMBERS)?.child(AppConstant.MEMBERS_LIST)?.value as? ArrayList<MemberIdNameModel>
            }

            override fun onCancelled(dataSnapshot: DatabaseError?) {
            }

        })
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FriendListFragment.
     */
    // TODO: Rename and change types and number of parameters
    fun newInstance(param1: String, param2: String): FriendListFragment {
        val fragment = FriendListFragment()
        val args = Bundle()
//            args.putString(ARG_PARAM1, param1)
//            args.putString(ARG_PARAM2, param2)
        fragment.arguments = args
        return fragment
    }

    override fun onViewClick(position: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
