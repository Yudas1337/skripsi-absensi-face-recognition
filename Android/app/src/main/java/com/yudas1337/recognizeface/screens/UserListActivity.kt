package com.yudas1337.recognizeface.screens

import android.os.Bundle
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.yudas1337.recognizeface.R
import com.yudas1337.recognizeface.databinding.ActivityUserListBinding

class UserListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityUserListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_user_list)

        navView.setupWithNavController(navController)
    }
}