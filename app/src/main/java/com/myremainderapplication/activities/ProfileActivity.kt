package com.myremainderapplication.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.myremainderapplication.R
import com.myremainderapplication.interfaces.AppConstant
import com.myremainderapplication.models.MemberFullInfoModel
import com.myremainderapplication.utils.ModelInfoUtils
import kotlinx.android.synthetic.main.activity_profile.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso


class ProfileActivity : AppCompatActivity() {
    private var id: String = ""
    private var memberFullInfoModel: MemberFullInfoModel? = null
    private var storageRef: StorageReference? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        id = intent.getStringExtra(AppConstant.MEMBER_ID)
        setDatabaseData()

        etCreateEvent.setOnClickListener{
            val intent=Intent(this@ProfileActivity,EventActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setDatabaseData() {
        val database = FirebaseDatabase.getInstance().reference
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot?) {
                memberFullInfoModel = ModelInfoUtils.Companion.getMemberInfoModel(dataSnapshot, id)
                updateData()
            }

            override fun onCancelled(dataSnapshot: DatabaseError?) {
            }

        })
    }

    private fun updateData() {
        tvName.text = memberFullInfoModel!!.memberName
        tvEmail.text = memberFullInfoModel!!.emailId
        tvMobileNumber.text = memberFullInfoModel!!.phoneNumber
        val url=memberFullInfoModel!!.imagePath
        Picasso.with(this)
                .load(url)
                .into(ivProfile)
    }

}
