package com.myremainderapplication.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.myremainderapplication.R

/**
 * <h1><font color="orange">SplashActivity</font></h1>
 * This Activity class is used for show a splash screen
 *
 * @author Shubham Chauhan
 */
class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
    }
}
