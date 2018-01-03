package com.myremainderapplication.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.myremainderapplication.R
import com.myremainderapplication.interfaces.AppConstant
import com.myremainderapplication.models.MemberInfoModel
import com.myremainderapplication.utils.ModelInfoUtils
import kotlinx.android.synthetic.main.activity_profile.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso


class ProfileActivity : AppCompatActivity() {
    private var id: String = ""
    private var memberInfoModel: MemberInfoModel? = null
    private var storageRef: StorageReference? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        storageRef = FirebaseStorage.getInstance().reference
        downloadProfileImage()

        id = intent.getStringExtra(AppConstant.MEMBER_ID)
        setDatabaseData()
    }

    private fun setDatabaseData() {
        val database = FirebaseDatabase.getInstance().reference
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot?) {
                memberInfoModel = ModelInfoUtils.Companion.getMemberInfoModel(dataSnapshot, id)
                updateData()
            }

            override fun onCancelled(dataSnapshot: DatabaseError?) {
            }

        })
    }

    private fun updateData() {
        tvName.text = memberInfoModel!!.memberName
        tvEmail.text = memberInfoModel!!.emailId
        tvMobileNumber.text = memberInfoModel!!.phoneNumber
    }

    private fun downloadProfileImage() {
        val imageRef = storageRef!!.child("images/3788.jpg")
        imageRef.downloadUrl
                .addOnSuccessListener {uri ->
                    val url=uri.toString()
                    Picasso.with(this)
                            .load(url)
                            .into(ivProfile)
                }.addOnFailureListener { exception ->
        }
    }

}
