package com.yudas1337.recognizeface.screens

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
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

        val cursor = dbHelper.getStudents()

        if (cursor != null) {
            val nameIndex = cursor.getColumnIndex("name")

            while (cursor.moveToNext()) {
                // Memeriksa apakah index ≥ 0 sebelum mengakses data dari cursor
                if (nameIndex >= 0 ) {
                    val name = cursor.getString(nameIndex)
                    // Tampilkan atau lakukan sesuatu dengan data yang ditemukan
                    Log.d("nama", "Name: $name")
                }
            }
            cursor.close()
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