package com.myremainderapplication.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

import com.myremainderapplication.R
import com.myremainderapplication.models.MemberInfoModel
import com.myremainderapplication.utils.ModelInfoUtils
import kotlinx.android.synthetic.main.fragment_profile.view.*

/**
 * A simple [Fragment] subclass.
 */
class ProfileFragment : Fragment() {
    private var id: String = ""
    private var memberInfoModel: MemberInfoModel? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        id="4041"
        setDatabaseData(view)
        return view
    }

    private fun setDatabaseData(view: View) {
        val database = FirebaseDatabase.getInstance().reference
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot?) {
                memberInfoModel = ModelInfoUtils.Companion.getMemberInfoModel(dataSnapshot, id)
                updateData(view)
            }

            override fun onCancelled(dataSnapshot: DatabaseError?) {
            }

        })
    }

    private fun updateData(view: View) {
        view.tvName.text = memberInfoModel!!.memberName
        view.tvEmail.text = memberInfoModel!!.emailId
        view.tvMobileNumber.text = memberInfoModel!!.phoneNumber
    }
}
