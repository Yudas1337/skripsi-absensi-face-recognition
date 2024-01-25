package com.yudas1337.recognizeface.screens

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.yudas1337.recognizeface.R
import com.yudas1337.recognizeface.database.DBHelper
import com.yudas1337.recognizeface.databinding.ActivityUserListBinding
import com.yudas1337.recognizeface.services.ApiService

class UserListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val dbHelper = DBHelper(this, null)

        ApiService(this).getStudents()

        if(dbHelper.countTableData("schedules") == 0){
            ApiService(this).getSchedules()
        }

        val buttonBack = findViewById<Button>(R.id.btn_bck)

        buttonBack.setOnClickListener {
            startActivity(Intent(this, MenuActivity::class.java))
            finish()
        }

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_user_list)

        navView.setupWithNavController(navController)
    }
}