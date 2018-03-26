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
import com.myremainderapplication.utils.ModelInfoUtils
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

        storageRef = FirebaseStorage.getInstance().reference
        setTextChangeListener()
        setDatabaseData()

        btSubmit.setOnClickListener {
            if (checkValidation()) {

                val gender: String = when {
                    rbMail.isChecked -> rbMail.text.toString()
                    else -> rbFemale.text.toString()
                }

                ModelInfoUtils.uploadNewUserData(currentMemberId,currentMemberListId,gender,etName.text.toString(),etMobileNumber.text.toString(),etEmail.text.toString(),
                etPassword.text.toString(),downloadUrl, SharedPreferencesUtils.getRegistrationKey(this)!!)
                finish()
            }
        }

        ivProfile.setOnClickListener {
            selectImageDialog()
        }
    }

    /**
     * Method is used to upload profile image in storage and get URL
     * @param file
     */
    private fun uploadProfileImage(file: Uri) {
        val childRef = storageRef!!.child("images/" + (currentMemberId.toInt() + 1).toString())
        val uploadTask = childRef.putFile(file)

        uploadTask.addOnFailureListener {
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

    /**
     * Method is used to show an Alert Dialog for select Profile image
     */
    private fun selectImageDialog() {
        val items = arrayOf(getString(R.string.take_photo), getString(R.string.choose_from_library), getString(R.string.cancel))
        val builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.add_photo))

        builder.setItems(items) { dialog, item ->
            when {
                items[item] == getString(R.string.take_photo) -> takePhotoFromCamera()
                items[item] == getString(R.string.choose_from_library) -> selectImageFromAlbum()
                else -> dialog.dismiss()
            }
        }
        builder.show()
    }

    /**
     * Method is used to select image from Album or Gallery
     */
    private fun selectImageFromAlbum() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        startActivityForResult(intent, AppConstant.REQUEST_SELECT_IMAGE_FROM_ALBUM)
    }

    /**
     * Method is used to take photo from Camera
     */
    private fun takePhotoFromCamera() {
        val imagesFolder = File(Environment.getExternalStorageDirectory(), getString(R.string.myimages))
        imagesFolder.mkdir()

        if (currentMemberId != "")
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

    /**
     * Method is used to take photo from Camera
     */
    private fun setTextChangeListener() {
        etName.addTextChangedListener(this)
        etMobileNumber.addTextChangedListener(this)
        etEmail.addTextChangedListener(this)
        etPassword.addTextChangedListener(this)
        etPasswordConfirm.addTextChangedListener(this)
    }

    /**
     * Method is used to get data(currentMemberId,currentMemberListId) from database
     */
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

    /**
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

    /**
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

    /**
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

    /**
     * This method will check validation for Name.
     */
    private fun checkValidationForName() {
        when {
            etName.text.toString().trim({ it <= ' ' }).isEmpty() -> {
                inputLayoutName.error = getString(R.string.please_enter_name)
                inputLayoutName.isErrorEnabled = true
                isChecked = false
            }
            else -> inputLayoutName.isErrorEnabled = false
        }
    }

    /**
     * This method will check validation for Mobile Number.
     */
    private fun checkValidationForMobileNumber() {
        when {
            etMobileNumber.text.toString().trim({ it <= ' ' }).isEmpty() -> {
                inputLayoutMobile.error = getString(R.string.please_enter_mobile_Number)
                inputLayoutMobile.isErrorEnabled = true
                isChecked = false
            }
            etMobileNumber.text.toString().trim({ it <= ' ' }).length < 10 -> {
                inputLayoutMobile.error = getString(R.string.please_enter_valid_mobile_number)
                inputLayoutMobile.isErrorEnabled = true
                isChecked = false
            }
            else -> inputLayoutMobile.isErrorEnabled = false
        }
    }

    /**
     * This method will check validation for Email Id.
     */
    private fun checkValidatioForEmail() {
        when {
            etEmail.text.toString().trim({ it <= ' ' }).isEmpty() -> {
                inputLayoutEmail.error = getString(R.string.please_enter_email_id)
                inputLayoutEmail.isErrorEnabled = true
                isChecked = false
            }
            !etEmail.text.toString().trim({ it <= ' ' }).matches(AppConstant.EMAIL_PATTERN.toRegex()) -> {
                inputLayoutEmail.error = getString(R.string.please_enter_valid_email_id)
                inputLayoutEmail.isErrorEnabled = true
                isChecked = false
            }
            else -> inputLayoutEmail.isErrorEnabled = false
        }
    }

    /**
     * This method will check validation for Password.
     */
    private fun checkValidationForPassword() {
        when {
            etPassword.text.toString().trim({ it <= ' ' }).isEmpty() -> {
                inputLayoutPassword.error = getString(R.string.please_enter_password)
                inputLayoutPassword.isErrorEnabled = true
                isChecked = false
            }
            else -> inputLayoutPassword.isErrorEnabled = false
        }
    }

    /**
     * This method will check validation for Confirm Password.
     */
    private fun checkValidationForConfirmPassword() {
        when {
            etPasswordConfirm.text.toString().trim({ it <= ' ' }).isEmpty() -> {
                inputLayoutPasswordConfirm.error = getString(R.string.please_enter_confirm_password)
                inputLayoutPasswordConfirm.isErrorEnabled = true
                isChecked = false
            }
            etPassword.text.toString().trim() != etPasswordConfirm.text.toString().trim() -> {
                inputLayoutPasswordConfirm.error = getString(R.string.please_enter_valid_confirm_password)
                inputLayoutPasswordConfirm.isErrorEnabled = true
                isChecked = false
            }
            else -> inputLayoutPasswordConfirm.isErrorEnabled = false
        }
    }

    /**
     * This method will check validation for Profile Image.
     */
    private fun checkValidationForImage() {
        when (downloadUrl) {
            "" -> isChecked = false
        }
    }


}
