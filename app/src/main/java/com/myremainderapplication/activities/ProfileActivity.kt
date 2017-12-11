package com.myremainderapplication.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.myremainderapplication.R
import com.myremainderapplication.interfaces.AppConstant
import com.myremainderapplication.models.MemberIdNameModel

class ProfileActivity : AppCompatActivity() {
    private var id:String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        id=intent.getStringExtra(AppConstant.MEMBER_ID)
        setDatabaseData()
    }

    private fun setDatabaseData() {
        val database = FirebaseDatabase.getInstance().reference
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot?) {
                 val data= dataSnapshot?.child(AppConstant.MEMBERS)!!.child(id).value as HashMap<String,String>
                val id=data.get(AppConstant.MEMBER_ID)
                val name=data.get(AppConstant.MEMBER_NAME)
                val phoneNumber=data.get(AppConstant.PHONE_NUMBER)
                val emailId=data.get(AppConstant.EMAIL_ID)
                val friendDataList=data.get(AppConstant.FRIEND_LIST) as ArrayList<*>
                val size = friendDataList.size

                var index = 0
                val friendList= ArrayList<MemberIdNameModel>()
                while (index < size) {
                    val hashMap = friendDataList.get(index) as HashMap<String, String>
                    val friendId = hashMap.get(AppConstant.MEMBER_ID).toString()
                    val friendName = hashMap.get(AppConstant.MEMBER_NAME)
                    friendList.add(MemberIdNameModel(friendId, friendName!!))
                    index++
                }

            }

            override fun onCancelled(dataSnapshot: DatabaseError?) {
            }

        })
    }
}
