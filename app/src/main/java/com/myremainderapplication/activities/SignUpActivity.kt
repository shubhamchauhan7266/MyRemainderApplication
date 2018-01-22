package com.myremainderapplication.activities

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.myremainderapplication.R
import com.myremainderapplication.interfaces.AppConstant
import kotlinx.android.synthetic.main.activity_sign_up.*
import com.google.firebase.storage.StorageReference
import java.io.File
import com.google.firebase.database.DatabaseReference
import com.myremainderapplication.utils.SharedPreferencesUtils
import com.squareup.picasso.Picasso

/**
 * <h1><font color="orange">SignUpActivity</font></h1>
 * This Activity class is used for Signup
 *
 * @author Shubham Chauhan
 */
class SignUpActivity : AppCompatActivity(), TextWatcher {

    private var isChecked: Boolean = false
    private var currentMemberId: String = ""
    private var currentMemberListId: String = ""

    private var storageRef: StorageReference? = null
    private lateinit var photoPath: File
    private var downloadUrl: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        storageRef = FirebaseStorage.getInstance().getReference()
        setTextChangeListener()
        setDatabaseData()

        btSubmit.setOnClickListener {
            if (checkValidation()) {
                uploadNewUserData()
                finish()
            }
        }

        ivProfile.setOnClickListener {
            selectImageDialog()
        }
    }

    private fun uploadNewUserData() {
        val databaseRef: DatabaseReference = FirebaseDatabase.getInstance().reference
        val databaseCurrentMemberIdRef = databaseRef.child(AppConstant.CURRENT_MEMBER_ID)
        val databaseCurrentMemberListIdRef = databaseRef.child(AppConstant.CURRENT_MEMBER_LIST_ID)
        val databaseMembersRef = databaseRef.child(AppConstant.MEMBERS)
        val databaseMemberListRef = databaseMembersRef.child(AppConstant.MEMBERS_LIST)

        val newMemberId = (currentMemberId.toInt() + 1).toString()
        val newMemberListId = (currentMemberListId.toInt() + 1).toString()

        val gender: String
        if (rbMail.isChecked)
            gender = rbMail.text.toString()
        else
            gender = rbFemale.text.toString()

        val hasMapMemberUserNode = HashMap<String, String>()
        hasMapMemberUserNode.put(AppConstant.MEMBER_ID, newMemberId)
        hasMapMemberUserNode.put(AppConstant.CURRENT_FRIEND_LIST_ID, (-1).toString())
        hasMapMemberUserNode.put(AppConstant.MEMBER_NAME, etName.text.toString())
        hasMapMemberUserNode.put(AppConstant.PHONE_NUMBER, etMobileNumber.text.toString())
        hasMapMemberUserNode.put(AppConstant.EMAIL_ID, etEmail.text.toString())
        hasMapMemberUserNode.put(AppConstant.PASSWORD, etPassword.text.toString())
        hasMapMemberUserNode.put(AppConstant.GENDER, gender)
        hasMapMemberUserNode.put(AppConstant.IMAGE_PATH, downloadUrl)
        hasMapMemberUserNode.put(AppConstant.REGISTRATION_TOKEN, SharedPreferencesUtils.getRegistrationKey(this)!!)

        val hasMapMemberNode = HashMap<String, HashMap<String, String>>()
        hasMapMemberNode.put(newMemberId, hasMapMemberUserNode)

        val hasMapMemberListUserNode = HashMap<String, String>()
        hasMapMemberListUserNode.put(AppConstant.MEMBER_ID, newMemberId)
        hasMapMemberListUserNode.put(AppConstant.MEMBER_NAME, etName.text.toString())
        hasMapMemberListUserNode.put(AppConstant.IMAGE_PATH, downloadUrl)
        hasMapMemberListUserNode.put(AppConstant.REGISTRATION_TOKEN, SharedPreferencesUtils.getRegistrationKey(this)!!)

        val hasMapMemberListNode = HashMap<String, HashMap<String, String>>()
        hasMapMemberListNode.put(newMemberListId, hasMapMemberListUserNode)

        databaseMembersRef.updateChildren(hasMapMemberNode as Map<String, Any>?)
        databaseMemberListRef.updateChildren(hasMapMemberListNode as Map<String, Any>?)
        databaseCurrentMemberIdRef.setValue(newMemberId)
        databaseCurrentMemberListIdRef.setValue(newMemberListId)

    }

    private fun uploadProfileImage(file: Uri) {
        val childRef = storageRef!!.child("images/" + file.lastPathSegment)
        val uploadTask = childRef.putFile(file)

        uploadTask.addOnFailureListener { exception ->
            // Handle unsuccessful uploads
        }.addOnSuccessListener { taskSnapshot ->
            //TODO has to be managed in background
            downloadUrl = (taskSnapshot.downloadUrl).toString()
            Picasso.with(this)
                    .load(downloadUrl)
                    .centerInside()
                    .resize(100, 100)
                    .into(ivProfile)
        }
    }

    private fun selectImageDialog() {
        val items = arrayOf(getString(R.string.take_photo), getString(R.string.choose_from_library), getString(R.string.cancel))
        val builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.add_photo))
        builder.setItems(items) { dialog, item ->
            if (items[item].equals(getString(R.string.take_photo))) {
                takePhotoFromCamera()
            } else if (items[item].equals(getString(R.string.choose_from_library))) {
                selectImageFromAlbum()
            } else {
                dialog.dismiss()
            }
        }
        builder.show()
    }

    private fun selectImageFromAlbum() {
        val intent = Intent()
        intent.setAction(Intent.ACTION_GET_CONTENT)
        intent.setType("image/*")
        startActivityForResult(intent, AppConstant.REQUEST_SELECT_IMAGE_FROM_ALBUM)
    }

    private fun takePhotoFromCamera() {
        val imagesFolder = File(Environment.getExternalStorageDirectory(), getString(R.string.myimages))
        imagesFolder.mkdir()
        if (!currentMemberId.equals(""))
            photoPath = File(imagesFolder, (currentMemberId.toInt() + 1).toString() + ".jpg")
        else
            return
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoPath))
        startActivityForResult(intent, AppConstant.REQUEST_TAKE_PHOTO)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            var uri: Uri? = null
            when (requestCode) {
                AppConstant.REQUEST_SELECT_IMAGE_FROM_ALBUM -> {
                    uri = data!!.data
                }
                AppConstant.REQUEST_TAKE_PHOTO -> {
                    uri = Uri.fromFile(photoPath)
                }
            }
            uploadProfileImage(uri!!)
        }
    }

    private fun setTextChangeListener() {
        etName.addTextChangedListener(this)
        etMobileNumber.addTextChangedListener(this)
        etEmail.addTextChangedListener(this)
        etPassword.addTextChangedListener(this)
        etPasswordConfirm.addTextChangedListener(this)
    }

    private fun setDatabaseData() {
        val database = FirebaseDatabase.getInstance().reference
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot?) {
                currentMemberId = dataSnapshot?.child(AppConstant.CURRENT_MEMBER_ID)?.value.toString()
                currentMemberListId = dataSnapshot?.child(AppConstant.CURRENT_MEMBER_LIST_ID)?.value.toString()
            }

            override fun onCancelled(dataSnapshot: DatabaseError?) {
            }

        })
    }

    override fun afterTextChanged(p0: Editable?) {
    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        checkValidationBeforeTextChanged()
    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        checkValidationOnTextChanged()
    }

    /*
     * Method is called when we click on the submit button
     * it validate all required fields.
     * return true if all required field are valid otherwise return false.
     */
    private fun checkValidation(): Boolean {
        isChecked = true
        checkValidationForName()
        checkValidationForMobileNumber()
        checkValidatioForEmail()
        checkValidationForPassword()
        checkValidationForConfirmPassword()
        checkValidationForImage()
        return isChecked
    }

    /*
     * This method will check validation before text changed.
     * if user missed above field and type on next field then it will show error on above field.
     */
    private fun checkValidationBeforeTextChanged() {
        if (etMobileNumber.hasFocus() || etEmail.hasFocus() || etPassword.hasFocus() || etPasswordConfirm.hasFocus()) {
            checkValidationForName()
        }

        if (etEmail.hasFocus() || etPassword.hasFocus() || etPasswordConfirm.hasFocus()) {
            checkValidationForMobileNumber()
        }

        if (etPassword.hasFocus() || etPasswordConfirm.hasFocus()) {
            checkValidatioForEmail()
        }

        if (etPasswordConfirm.hasFocus()) {
            checkValidationForPassword()
        }

    }

    /*
     * This method will check validation on text changed.
     */
    private fun checkValidationOnTextChanged() {
        if (etName.hasFocus()) {
            checkValidationForName()
        }

        if (etMobileNumber.hasFocus()) {
            checkValidationForMobileNumber()
        }

        if (etEmail.hasFocus()) {
            checkValidatioForEmail()
        }

        if (etPassword.hasFocus()) {
            checkValidationForPassword()
        }

        if (etPasswordConfirm.hasFocus()) {
            checkValidationForConfirmPassword()
        }

    }

    /*
     * This method will check validation for Name.
     */
    internal fun checkValidationForName() {
        if (etName.getText().toString().trim({ it <= ' ' }).length == 0) {
            inputLayoutName.error = getString(R.string.please_enter_name)
            inputLayoutName.setErrorEnabled(true)
            isChecked = false
        } else {
            inputLayoutName.setErrorEnabled(false)
        }
    }

    /*
     * This method will check validation for Mobile Number.
     */
    internal fun checkValidationForMobileNumber() {
        if (etMobileNumber.getText().toString().trim({ it <= ' ' }).length == 0) {
            inputLayoutMobile.error = getString(R.string.please_enter_mobile_Number)
            inputLayoutMobile.setErrorEnabled(true)
            isChecked = false
        } else if (etMobileNumber.getText().toString().trim({ it <= ' ' }).length < 10) {
            inputLayoutMobile.error = getString(R.string.please_enter_valid_mobile_number)
            inputLayoutMobile.setErrorEnabled(true)
            isChecked = false
        } else {
            inputLayoutMobile.setErrorEnabled(false)
        }
    }

    /*
     * This method will check validation for Email Id.
     */
    internal fun checkValidatioForEmail() {
        if (etEmail.getText().toString().trim({ it <= ' ' }).length == 0) {
            inputLayoutEmail.error = getString(R.string.please_enter_email_id)
            inputLayoutEmail.setErrorEnabled(true)
            isChecked = false
        } else if (!etEmail.getText().toString().trim({ it <= ' ' }).matches(AppConstant.EMAIL_PATTERN.toRegex())) {
            inputLayoutEmail.setError(getString(R.string.please_enter_valid_email_id))
            inputLayoutEmail.setErrorEnabled(true)
            isChecked = false
        } else {
            inputLayoutEmail.setErrorEnabled(false)
        }
    }

    /*
    * This method will check validation for Password.
    */
    internal fun checkValidationForPassword() {
        if (etPassword.getText().toString().trim({ it <= ' ' }).length == 0) {
            inputLayoutPassword.setError(getString(R.string.please_enter_password))
            inputLayoutPassword.setErrorEnabled(true)
            isChecked = false
        } else {
            inputLayoutPassword.setErrorEnabled(false)
        }
    }

    /*
    * This method will check validation for Confirm Password.
    */
    internal fun checkValidationForConfirmPassword() {
        if (etPasswordConfirm.getText().toString().trim({ it <= ' ' }).length == 0) {
            inputLayoutPasswordConfirm.error = getString(R.string.please_enter_confirm_password)
            inputLayoutPasswordConfirm.setErrorEnabled(true)
            isChecked = false
        } else {
            inputLayoutPasswordConfirm.setErrorEnabled(false)
        }
    }

    /*
    * This method will check validation for Profile Image.
    */
    internal fun checkValidationForImage() {
        if (downloadUrl.equals(""))
            isChecked = false
    }


}
