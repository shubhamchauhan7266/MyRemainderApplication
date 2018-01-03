package com.myremainderapplication.activities

import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
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


class SignUpActivity : AppCompatActivity(), TextWatcher {

    private var isChecked: Boolean = false
    private var currentId: String = ""
    private var storageRef: StorageReference? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        storageRef = FirebaseStorage.getInstance().getReference();
        setTextChangeListener()
        setDatabaseData()

        btSubmit.setOnClickListener {
            if (checkValidation()) {

                finish()
            }
        }
    }

    private fun uplodeProfileImage(path:String) {

        val file = Uri.fromFile(File(path))
        val riversRef = storageRef!!.child("images/rivers.jpg")

        riversRef.putFile(file)
                .addOnSuccessListener { taskSnapshot ->
                    val downloadUrl = taskSnapshot.downloadUrl

                }.addOnFailureListener { exception ->
        }
    }

    private fun setTextChangeListener() {
        etName.addTextChangedListener(this)
        etMobileNumber.addTextChangedListener(this)
        etEmail.addTextChangedListener(this)
        etPassword.addTextChangedListener(this)
        etPasswordConfirm.addTextChangedListener(this)
        etZip.addTextChangedListener(this)
    }

    private fun setDatabaseData() {
        val database = FirebaseDatabase.getInstance().reference
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot?) {
                currentId = dataSnapshot?.child(AppConstant.CURRENT_ID)?.value.toString()
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
        checkValidatioForZip()
        return isChecked
    }

    /*
     * This method will check validation before text changed.
     * if user missed above field and type on next field then it will show error on above field.
     */
    private fun checkValidationBeforeTextChanged() {
        if (etMobileNumber.hasFocus() || etEmail.hasFocus() || etPassword.hasFocus() || etPasswordConfirm.hasFocus() || etZip.hasFocus()) {
            checkValidationForName()
        }

        if (etEmail.hasFocus() || etPassword.hasFocus() || etPasswordConfirm.hasFocus() || etZip.hasFocus()) {
            checkValidationForMobileNumber()
        }

        if (etPassword.hasFocus() || etPasswordConfirm.hasFocus() || etZip.hasFocus()) {
            checkValidatioForEmail()
        }

        if (etPasswordConfirm.hasFocus() || etZip.hasFocus()) {
            checkValidationForPassword()
        }

        if (etZip.hasFocus()) {
            checkValidationForConfirmPassword()
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

        if (etZip.hasFocus()) {
            checkValidatioForZip()
        }
    }

    /*
     * This method will check validation for Name.
     */
    internal fun checkValidationForName() {
        if (etName.getText().toString().trim({ it <= ' ' }).length == 0) {
            inputLayoutName.setError(AppConstant.NAME_VALIDATION_ERROR)
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
            inputLayoutMobile.setError(AppConstant.MOBILE_NUMBER_VALIDATION_ERROR)
            inputLayoutMobile.setErrorEnabled(true)
            isChecked = false
        } else if (etMobileNumber.getText().toString().trim({ it <= ' ' }).length < 10) {
            inputLayoutMobile.setError(AppConstant.NOT_VALID_MOBILE_NUMBER_ERROR)
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
            inputLayoutEmail.setError(AppConstant.EMAIL_ID_VALIDATION_ERROR)
            inputLayoutEmail.setErrorEnabled(true)
            isChecked = false
        } else if (!etEmail.getText().toString().trim({ it <= ' ' }).matches(AppConstant.EMAIL_PATTERN.toRegex())) {
            inputLayoutEmail.setError(AppConstant.NOT_VALID_EMAIL_ID_ERROR)
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
            inputLayoutPassword.setError(AppConstant.NAME_VALIDATION_ERROR)
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
            inputLayoutPasswordConfirm.setError(AppConstant.CONFIRM_PASSWORD_VALIDATION_ERROR)
            inputLayoutPasswordConfirm.setErrorEnabled(true)
            isChecked = false
        } else {
            inputLayoutPasswordConfirm.setErrorEnabled(false)
        }
    }

    /*
     * This method will check validation for Zip.
     */
    internal fun checkValidatioForZip() {
        if (etZip.getText().toString().trim({ it <= ' ' }).length == 0) {
            inputLayoutZip.setError(AppConstant.ZIP_VALIDATION_ERROR)
            inputLayoutZip.setErrorEnabled(true)
            isChecked = false
        } else {
            inputLayoutZip.setErrorEnabled(false)
        }
    }
}
