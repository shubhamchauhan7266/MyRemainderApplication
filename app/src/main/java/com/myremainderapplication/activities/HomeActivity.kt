package com.myremainderapplication.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.myremainderapplication.R
import com.myremainderapplication.fragments.FriendListFragment
import kotlinx.android.synthetic.main.activity_home.*
import com.myremainderapplication.fragments.ProfileFragment
import com.myremainderapplication.fragments.HomeFragment
import com.myremainderapplication.fragments.NotifactionFragment


class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)


        bottomNavigationView.setOnNavigationItemSelectedListener{item ->
            when(item.itemId){
                R.id.action_home -> {
                    loadHomeFragment()
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.action_profile -> {
                    loadProfileFragment()
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.action_notification -> {
                    loadNotifactionFragment()
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.action_friends->{
                    loadFriendListFragment()
                    return@setOnNavigationItemSelectedListener true
                }
                else->{
                    return@setOnNavigationItemSelectedListener false
                }
            }
        }

    }

    private fun loadFriendListFragment() {
        val fragment = FriendListFragment()
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.fragmentContainer, fragment)
        ft.commit()
    }

    private fun loadHomeFragment() {
        val fragment = HomeFragment()
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.fragmentContainer, fragment)
        ft.commit()
    }

    private fun loadProfileFragment() {
        val fragment = ProfileFragment()
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.fragmentContainer, fragment)
        ft.commit()
    }

    private fun loadNotifactionFragment() {
        val fragment = NotifactionFragment()
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.fragmentContainer, fragment)
        ft.commit()
    }
}
