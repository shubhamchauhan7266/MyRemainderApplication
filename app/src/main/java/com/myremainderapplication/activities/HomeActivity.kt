package com.myremainderapplication.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.ActionBar
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.myremainderapplication.R
import com.myremainderapplication.fragments.FriendListFragment
import kotlinx.android.synthetic.main.activity_home.*
import com.myremainderapplication.fragments.ProfileFragment
import com.myremainderapplication.fragments.HomeFragment
import com.myremainderapplication.fragments.NotificationFragment
import com.myremainderapplication.interfaces.AppConstant
import com.myremainderapplication.utils.ModelInfoUtils
import com.myremainderapplication.utils.SharedPreferencesUtils

/**
 * <h1><font color="orange">HomeActivity</font></h1>
 * This Activity class is used for contain fragments ehich are FriendListFragment,HomeFragment,
 * NotificationFragment,ProfileFragment
 *
 * @author Shubham Chauhan
 */
class HomeActivity : AppCompatActivity() {
    private lateinit var actionBar: ActionBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        setUpToolbar()

        var isUpdateRequired = false
        val database = FirebaseDatabase.getInstance().reference.child(AppConstant.MEMBERS)
        database.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot?) {
                if (!isUpdateRequired) {
                    isUpdateRequired = true
                    updateNewRegistrationToken(dataSnapshot!!)
                }
            }

            override fun onCancelled(dataSnapshot: DatabaseError?) {
            }

        })

        loadHomeFragment()

        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {

                R.id.action_home -> {
                    loadHomeFragment()
                    actionBar.title = getString(R.string.home)
                    return@setOnNavigationItemSelectedListener true
                }

                R.id.action_profile -> {
                    loadProfileFragment()
                    actionBar.title = getString(R.string.profile)
                    return@setOnNavigationItemSelectedListener true
                }

                R.id.action_notification -> {
                    loadNotifactionFragment()
                    actionBar.title = getString(R.string.notification)
                    return@setOnNavigationItemSelectedListener true
                }

                R.id.action_friends -> {
                    loadFriendListFragment()
                    actionBar.title = getString(R.string.friends)
                    return@setOnNavigationItemSelectedListener true
                }

                else -> {
                    return@setOnNavigationItemSelectedListener false
                }
            }
        }
    }

    private fun setUpToolbar() {
        setSupportActionBar(toolbar as Toolbar?)
        actionBar = supportActionBar!!
        actionBar.title = getString(R.string.home)
        actionBar.setDisplayHomeAsUpEnabled(true)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.home_toolbar_item, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            android.R.id.home->{
                finish()
                true
            }
            R.id.action_search -> {
                true
            }
            R.id.action_set_alarm -> {
                true
            }
            R.id.action_logout -> {
                true
            }
            else -> {
                false
            }
        }
    }

    /**
     * Method is used to load FriendListFragment
     */
    private fun loadFriendListFragment() {
        val fragment = FriendListFragment()
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.fragmentContainer, fragment)
        ft.commit()
    }

    /**
     * Method is used to load HomeFragment
     */
    private fun loadHomeFragment() {
        val fragment = HomeFragment()
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.fragmentContainer, fragment)
        ft.commit()
    }

    /**
     * Method is used to load ProfileFragment
     */
    private fun loadProfileFragment() {
        val fragment = ProfileFragment()
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.fragmentContainer, fragment)
        ft.commit()
    }

    /**
     * Method is used to load NotifactionFragment
     */
    private fun loadNotifactionFragment() {
        val fragment = NotificationFragment()
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.fragmentContainer, fragment)
        ft.commit()
    }

    /**
     * Method is used to update Registration/Refresh token which is changed when app data is cleared
     * @param dataSnapshot
     */
    private fun updateNewRegistrationToken(dataSnapshot: DataSnapshot) {
        val memberId = SharedPreferencesUtils.getMemberId(this).toString()

        val memberList = dataSnapshot.child(AppConstant.MEMBERS_LIST)?.value as ArrayList<*>
        val memberUserList = ModelInfoUtils.Companion.getMemberList(memberList)

        var index = 0
        while (index < memberUserList.size) {
            if (memberId == memberUserList[index].memberId) {
                ModelInfoUtils.updateRegistrationToken(this, memberId, index.toString())
                break
            }
            index++
        }
    }
}
