package com.yudas1337.recognizeface.screens.intro

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleObserver
import com.yudas1337.recognizeface.R
import com.yudas1337.recognizeface.database.DBHelper
import com.yudas1337.recognizeface.helpers.AlertHelper
import com.yudas1337.recognizeface.network.NetworkConnection
import com.yudas1337.recognizeface.screens.MenuActivity
import com.yudas1337.recognizeface.services.ApiService

class SplashActivity : AppCompatActivity(), LifecycleObserver {
    private var splashTimeout = 2000
    private lateinit var networkConnection: NetworkConnection
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        val dbHelper = DBHelper(this, null)
        networkConnection = NetworkConnection(this)
        lifecycle.addObserver(this)
        networkConnection.observe(this){
            if(it){
                this.fetchData(dbHelper)
                splashTimeout = 500
                if(dbHelper.countTableData("attendances") == 0){
                    AlertHelper.internetAvailable(this,
                        {
                            Toast.makeText(this, "OK CONFIRM", Toast.LENGTH_SHORT).show()
                            nextActivity()
                        },
                        { nextActivity() }
                    )
                } else{
                    nextActivity()
                }
            } else{
                nextActivity()
            }
        }

    }

    private fun nextActivity(){
        Handler().postDelayed({
            startActivity(Intent(this, MenuActivity::class.java))
            finish()
        }, splashTimeout.toLong())
    }

    private fun fetchData(dbHelper: DBHelper){
        if(dbHelper.countTableData("students") == 0){
            ApiService(this).getStudents()
        }

        if(dbHelper.countTableData("schedules") == 0){
            ApiService(this).getSchedules()
        }

        if(dbHelper.countTableData("employees") == 0){
            ApiService(this).getEmployees()
        }

        if(dbHelper.countTableData("attendance_rule") == 0){
            ApiService(this).getAttendanceLimit()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if(networkConnection.isInitialized){
            networkConnection.clearObserver()
        }
    }
}