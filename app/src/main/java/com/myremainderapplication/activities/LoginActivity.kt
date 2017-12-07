package com.myremainderapplication.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.myremainderapplication.R
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() ,View.OnClickListener {
    private val TAG= LoginActivity::class.java.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        btSignUp.setOnClickListener(this)
        btLogin.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when(view?.id){
            R.id.btSignUp->{
                val intent=Intent(this@LoginActivity,SignUpActivity::class.java)
                startActivity(intent)
            }
            R.id.btLogin->{
                val intent=Intent(this@LoginActivity,HomeActivity::class.java)
                startActivity(intent)
            }
            else->{
                Log.d(TAG,"Wrong case selection")
            }
        }
    }
}
