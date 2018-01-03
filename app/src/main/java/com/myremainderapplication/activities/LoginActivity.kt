package com.myremainderapplication.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.myremainderapplication.R
import kotlinx.android.synthetic.main.activity_login.*
import com.google.firebase.auth.FirebaseAuth
import android.widget.Toast

class LoginActivity : AppCompatActivity(), View.OnClickListener {
    private val TAG = LoginActivity::class.java.simpleName
    private var auth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance();

        /*createAcount("","")

        signInWithEmailId("","")

        userDetails()*/


        btSignUp.setOnClickListener(this)
        btLogin.setOnClickListener(this)
    }

    private fun userDetails() {
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            // Name, email address, and profile photo Url
            val name = user.displayName
            val email = user.email
            val photoUrl = user.photoUrl

            // Check if user's email is verified
            val emailVerified = user.isEmailVerified

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getToken() instead.
            val uid = user.uid
        }
    }

    private fun signInWithEmailId(emailId: String, password: String) {
        auth!!.signInWithEmailAndPassword(emailId, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d("", "signInWithEmail:success")
                        val user = auth!!.getCurrentUser()
//                        updateUI(user)
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w("", "signInWithEmail:failure", task.exception)
                        Toast.makeText(this@LoginActivity, "Authentication failed.",
                                Toast.LENGTH_SHORT).show()
//                        updateUI(null)
                    }

                    // ...
                }
    }

    private fun createAcount(emailId: String, password: String) {
        auth!!.createUserWithEmailAndPassword(emailId, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d("", "createUserWithEmail:success")
                        val user = auth!!.getCurrentUser()
//                        updateUI(user)
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w("", "createUserWithEmail:failure", task.exception)
                        Toast.makeText(this@LoginActivity, "Authentication failed.",
                                Toast.LENGTH_SHORT).show()
//                        updateUI(null)
                    }

                    // ...
                }
    }

   /* private fun updateUI(user: FirebaseUser?) {
        user!!.getIdToken(true)
    }*/

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth!!.getCurrentUser()
//        updateUI(currentUser)
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.btSignUp -> {
                val intent = Intent(this@LoginActivity, SignUpActivity::class.java)
                startActivity(intent)
            }
            R.id.btLogin -> {
                val intent = Intent(this@LoginActivity, HomeActivity::class.java)
                startActivity(intent)
            }
            else -> {
                Log.d(TAG, "Wrong case selection")
            }
        }
    }
}
