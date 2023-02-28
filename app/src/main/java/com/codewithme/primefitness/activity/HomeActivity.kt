package com.codewithme.primefitness.activity

import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.PersistableBundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.codewithme.primefitness.R
import com.codewithme.primefitness.databinding.ActivityHomeBinding
import com.codewithme.primefitness.fragment.*
import com.example.primefitness.fragment.*
import com.codewithme.primefitness.global.DB
import com.codewithme.primefitness.manager.SessionManager
import com.google.android.material.navigation.NavigationView
import java.lang.Exception
import kotlin.math.log

class HomeActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private val TAG = "HomeActivity"
    var session: SessionManager? = null
    var db: DB? = null
    private lateinit var drawer: DrawerLayout
    private lateinit var toggle: ActionBarDrawerToggle
    lateinit var binding: ActivityHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = DB(this)
        setSupportActionBar(binding.homeInclude.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.navView.setNavigationItemSelectedListener(this)
        drawer = binding.drawerLayout

        toggle = ActionBarDrawerToggle(
            this, drawer, binding.homeInclude.toolbar,
            R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )

        drawer.addDrawerListener(toggle)
        toggle.syncState()

        val fragment = FragmentAllMember()
        loadFragment(fragment)
    }

    override fun onPostCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onPostCreate(savedInstanceState, persistentState)
        toggle.syncState()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        toggle.onConfigurationChanged(newConfig)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        if(item.itemId == R.id.logOutMenu){
            logOut()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_home -> {
//                Toast.makeText(this, "Home", Toast.LENGTH_LONG).show()
                val fragment = FragmentAllMember()
                loadFragment(fragment)
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START)
                }
            }
            R.id.nav_add -> {
//                Toast.makeText(this, "Add", Toast.LENGTH_LONG).show()
//                val fragment = FragmentAddMember()
//                loadFragment(fragment)
                loadFragment()
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START)
                }

            }
            R.id.nav_nav_fee_pending -> {
//                Toast.makeText(this, "Fee Pending", Toast.LENGTH_LONG).show()
                val fragment = FragmentFeePending()
                loadFragment(fragment)
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START)
                }
            }
//            R.id.nav_update_fee -> {
////                Toast.makeText(this, "Update Fee", Toast.LENGTH_LONG).show()
//                val fragment = FragmentAppUpdateFee()
//                loadFragment(fragment)
//                if (drawer.isDrawerOpen(GravityCompat.START)) {
//                    drawer.closeDrawer(GravityCompat.START)
//                }
//            }
            R.id.nav_change_password -> {
//                Toast.makeText(this, "Change Password", Toast.LENGTH_LONG).show()
                val fragment = FragmentChangePassword()
                loadFragment(fragment)
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START)
                }
            }
            R.id.nav_log_out -> {
//                Toast.makeText(this, "Log Out", Toast.LENGTH_LONG).show()
                logOut()
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START)
                }
            }
            R.id.nav_import_export_database -> {
//                Toast.makeText(this, "Home", Toast.LENGTH_LONG).show()
                val fragment = FragmentImportExportDatabase()
                loadFragment(fragment)
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START)
                }
            }
        }

        return true
    }

    private fun logOut(){
        session?.setLogin(false)
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    private var doubleBackToExitPressedOnce = false
    override fun onBackPressed() {
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START)
        }else{
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed()
                return
            }
            this.doubleBackToExitPressedOnce = true
            Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show()
            Handler(Looper.getMainLooper()).postDelayed(Runnable { doubleBackToExitPressedOnce = false }, 2000)
        }
    }

    private fun loadFragment(fragment: Fragment){
        var fragmentManager: FragmentManager?= null
        fragmentManager = supportFragmentManager
        fragmentManager.beginTransaction().replace(R.id.frame_container, fragment, "Home").commit()
    }

    private fun loadFragment(){
        val fragment = FragmentAddMember()
        val args = Bundle()
        args.putString("ID", "")
        fragment.arguments = args
        val fragmentManager:FragmentManager = supportFragmentManager
        fragmentManager.beginTransaction().replace(R.id.frame_container, fragment, "FragmentAdd").commit()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        try{
            val infalter = menuInflater
            infalter.inflate(R.menu.menu_main, menu)
        }catch (e:Exception){
            e.printStackTrace()
        }
        return super.onCreateOptionsMenu(menu)
    }

}