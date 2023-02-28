package com.codewithme.primefitness

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Binder
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.renderscript.ScriptGroup
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModelProvider
import com.codewithme.primefitness.R
import com.codewithme.primefitness.databinding.ActivityMainBinding
import com.codewithme.primefitness.activity.HomeActivity
import com.codewithme.primefitness.activity.LoginActivity
import com.codewithme.primefitness.global.DB
import com.codewithme.primefitness.manager.SessionManager
import java.lang.Exception

class SplashScreenActivity : AppCompatActivity() {

    private var mDelayHandler: Handler? = null
    private val splashDelay: Long = 1000 // 3 seconds
    var db: DB? = null
    var session: SessionManager? = null
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = DB(this)
        session = SessionManager(this)

//        createNotificationChannel()
        insertAdminData()

        mDelayHandler = Handler()
        mDelayHandler?.postDelayed(mRunnable, splashDelay)
    }

    private val mRunnable: Runnable = Runnable {
        if(true){
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun insertAdminData() {
        try {
            val sqlCheck = "SELECT * FROM ADMIN"
            db?.fireQuery(sqlCheck)?.use {
                if (it.count > 0) {
                    Log.d("SplashActivity", "data_available")
                } else {
                    val sqlQuery =
                        "INSERT OR REPLACE INTO ADMIN(ID,USER_NAME,PASSWORD,MOBILE) VALUES('1','sagarpomal','5000','7405761085')"
                    db?.executeQuery(sqlQuery)
                }
            }
            val sqlQuery =
                "INSERT OR REPLACE INTO ADMIN(ID,USER_NAME,PASSWORD,MOBILE) VALUES('1','sagarpomal','5000','7405761085')"
            db?.executeQuery(sqlQuery)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mDelayHandler?.removeCallbacks(mRunnable)
    }

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.channel_name)
            val descriptionText = getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(R.string.channel_id.toString(), name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}