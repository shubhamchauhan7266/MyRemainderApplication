package com.myremainderapplication.fragments

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

import com.myremainderapplication.R
import com.myremainderapplication.models.MemberFullInfoModel
import com.myremainderapplication.utils.ModelInfoUtils
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_profile.view.*
import com.myremainderapplication.interfaces.AppConstant
import java.io.File


/**
 * A simple [Fragment] subclass.
 */
class ProfileFragment : Fragment() {
    private var id: String = ""
    private var memberFullInfoModel: MemberFullInfoModel? = null
    private var mContext: Context? = null
    private var mStorageRef: StorageReference? = null
    private lateinit var photoPath: File


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        /*if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED&&ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED&&ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(
                    arrayOf(Manifest.permission.CAMERA,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.INTERNET), 10)
            return
        }*/

        id = "4041"
        mStorageRef = FirebaseStorage.getInstance().reference
        setDatabaseData(view)
        view.ivProfile.setOnClickListener{
            selectImageDialog()
        }

        return view
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        mContext = context!!
    }

    private fun setDatabaseData(view: View) {
        val database = FirebaseDatabase.getInstance().reference
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot?) {
                memberFullInfoModel = ModelInfoUtils.Companion.getMemberInfoModel(dataSnapshot, id)
                updateData(view)
            }

            override fun onCancelled(dataSnapshot: DatabaseError?) {
            }

        })
    }

    private fun updateData(view: View) {
        view.tvName.text = memberFullInfoModel!!.memberName
        view.tvEmail.text = memberFullInfoModel!!.emailId
        view.tvMobileNumber.text = memberFullInfoModel!!.phoneNumber
        val url=memberFullInfoModel!!.imagePath
        Picasso.with(mContext)
                .load(url)
                .into(view.ivProfile)
    }

    private fun selectImageDialog(){
        val items= arrayOf("Take Photo", "Choose from Library", "Cancel")
        val builder= AlertDialog.Builder(mContext)
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
        startActivityForResult(intent, AppConstant.REQUEST_SELECT_IMAGE_FROM_ALBUM)
    }

    private fun takePhotoFromCamera(){
        val imagesFolder = File(Environment.getExternalStorageDirectory(), "MyImages")
        imagesFolder.mkdir()
        photoPath = File(imagesFolder, memberFullInfoModel!!.memberId + ".jpg")
        val intent= Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoPath))
        startActivityForResult(intent, AppConstant.REQUEST_TAKE_PHOTO)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode== Activity.RESULT_OK){
            var uri: Uri? =null
            when(requestCode){
                AppConstant.REQUEST_SELECT_IMAGE_FROM_ALBUM ->{
                    uri = data!!.data
                }
                AppConstant.REQUEST_TAKE_PHOTO ->{
                    uri= Uri.fromFile(photoPath)
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
