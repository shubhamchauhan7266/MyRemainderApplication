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
import com.squareup.picasso.Picasso

/**
 * <h1><font color="orange">ProfileActivity</font></h1>
 * This Activity class is used for display a Profile of Friend
 *
 * @author Shubham Chauhan
 */
class ProfileActivity : AppCompatActivity() {
    private var id: String = ""
    private var memberFullInfoModel: MemberFullInfoModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        id = intent.getStringExtra(AppConstant.MEMBER_ID)
        setDatabaseData()

        etCreateEvent.isClickable = false
        etCreateEvent.setOnClickListener {
            val intent = Intent(this@ProfileActivity, EventActivity::class.java)
            intent.putExtra(AppConstant.REGISTRATION_TOKEN, memberFullInfoModel?.registrationToken)
            startActivity(intent)
        }
    }

    /**
     * Method is used to get member data from database and then call updateData() method for update UI
     */
    private fun setDatabaseData() {
        val database = FirebaseDatabase.getInstance().reference.child(AppConstant.MEMBERS)

        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot?) {
                memberFullInfoModel = ModelInfoUtils.Companion.getMemberFullInfoModel(dataSnapshot, id)
                updateData()
            }

            override fun onCancelled(dataSnapshot: DatabaseError?) {
            }

        })
    }

    /**
     * Method is used to update UI
     */
    private fun updateData() {
        etCreateEvent.isClickable = true

        tvName.text = memberFullInfoModel!!.memberName
        tvEmail.text = memberFullInfoModel!!.emailId
        tvMobileNumber.text = memberFullInfoModel!!.phoneNumber
        val url = memberFullInfoModel!!.imagePath

        Picasso.with(this)
                .load(url)
                .resize(100, 100)
                .into(ivProfile)
    }

}
