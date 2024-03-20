package com.yudas1337.recognizeface.screens.intro

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleObserver
import com.yudas1337.recognizeface.R
import com.yudas1337.recognizeface.constants.ConstShared
import com.yudas1337.recognizeface.database.DBHelper
import com.yudas1337.recognizeface.network.NetworkConnection
import com.yudas1337.recognizeface.screens.MenuActivity
import com.yudas1337.recognizeface.services.ApiService
import com.yudas1337.recognizeface.services.CheckSumService

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity(), LifecycleObserver {
    private var splashTimeout = 2000
    private lateinit var networkConnection: NetworkConnection
    private lateinit var sumService: CheckSumService
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        val dbHelper = DBHelper(this, null)
        sharedPreferences = getSharedPreferences(ConstShared.fileName, Context.MODE_PRIVATE)
        sumService = CheckSumService(sharedPreferences)

        networkConnection = NetworkConnection(this)
        lifecycle.addObserver(this)
        networkConnection.observe(this){
            if(it){
                this.fetchData(dbHelper)
            }
            nextActivity()
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