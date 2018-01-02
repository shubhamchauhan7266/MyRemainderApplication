package com.myremainderapplication.activities

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore

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
import com.myremainderapplication.interfaces.AppConstant.Companion.REQUEST_SELECT_IMAGE_FROM_ALBUM
import com.myremainderapplication.interfaces.AppConstant.Companion.REQUEST_TAKE_PHOTO
import com.squareup.picasso.Picasso
import android.Manifest
import android.app.AlertDialog
import android.content.DialogInterface
import android.support.v4.app.ActivityCompat
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.support.annotation.RequiresApi
import android.support.v4.content.ContextCompat
import java.io.File
import java.net.URI


class ProfileActivity : AppCompatActivity() {
    private var id: String = ""
    private var memberInfoModel: MemberInfoModel? = null
    private var mStorageRef: StorageReference? = null

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)


        /*if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED&&ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED&&ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(
                    arrayOf(Manifest.permission.CAMERA,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.INTERNET), 10)
            return
        }*/
        ivProfile.setOnClickListener{
            selectImageDialog()
        }

        mStorageRef = FirebaseStorage.getInstance().reference
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
        val imageRef = mStorageRef!!.child("images/3788.jpg")
        imageRef.downloadUrl
                .addOnSuccessListener {uri ->
                    val url=uri.toString()
                    Picasso.with(this)
                            .load(url)
                            .into(ivProfile)
                }.addOnFailureListener { exception ->
        }
    }

    private fun selectImageDialog(){
        val items= arrayOf("Take Photo", "Choose from Library", "Cancel")
        val builder=AlertDialog.Builder(this)
        builder.setTitle("Add Photo!")
        builder.setItems(items){dialog,item->
            if(items[item].equals("Take Photo")){
                takePhotoFromCamera()
            }else if(items[item].equals("Choose from Library")){
                selectImageFromAlbum()
            }else{
                dialog.dismiss()
            }
        }
        builder.show()
    }

    private fun selectImageFromAlbum(){
        val intent= Intent()
        intent.setAction(Intent.ACTION_GET_CONTENT)
        intent.setType("image/*")
        startActivityForResult(Intent.createChooser(intent, "Select Picture"),REQUEST_SELECT_IMAGE_FROM_ALBUM)
    }

    private fun takePhotoFromCamera(){
        val intent= Intent()
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.setType("image/*")
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), REQUEST_TAKE_PHOTO)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode== Activity.RESULT_OK){
            var uri: Uri? =null
            when(requestCode){
                REQUEST_SELECT_IMAGE_FROM_ALBUM->{
                    uri = data!!.data
                }
                REQUEST_TAKE_PHOTO->{
                    uri= data!!.data
                }
                else->{

                }
            }
            uploadProfileImage(uri!!)
        }
    }

    private fun uploadProfileImage(file:Uri){
        val childRef = mStorageRef!!.child("images/" + file.lastPathSegment)
        val uploadTask = childRef.putFile(file)

        uploadTask.addOnFailureListener{exception->
            // Handle unsuccessful uploads
        }.addOnSuccessListener{ taskSnapshot ->
            val downloadUrl = taskSnapshot.downloadUrl
        }
    }

    /*override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 10) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
            }
        }
    }*/
}
