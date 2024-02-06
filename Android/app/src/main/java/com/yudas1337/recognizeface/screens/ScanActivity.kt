package com.yudas1337.recognizeface.screens

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.yudas1337.recognizeface.R
import com.yudas1337.recognizeface.database.DBHelper
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class ScanActivity : AppCompatActivity() {

    private lateinit var relativeOne: RelativeLayout
    private lateinit var rfidController : EditText
    private lateinit var copyRight: TextView
    private lateinit var btnSubmit: Button

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan)

        relativeOne = findViewById(R.id.relative_1)

        relativeOne.setOnClickListener{
            backService()
        }

        val dbHelper = DBHelper(this, null)

        copyRight = findViewById(R.id.cr_2)
        rfidController = findViewById(R.id.rfidController)
        btnSubmit = findViewById(R.id.btn_submit)
        val currentYear = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy"))
        val copyrightText = "$currentYear Hummatech All Rights Reserved."
        copyRight.text = copyrightText

        btnSubmit.setOnClickListener {
            val RFID_CARD = rfidController.text.toString()
            if (RFID_CARD.isNotEmpty()) {
                val students = dbHelper.findUsersByRfid("students", RFID_CARD)
                val employees = dbHelper.findUsersByRfid("employees", RFID_CARD)

                if (students != null && students.moveToFirst()) {
                    val indexName = students.getColumnIndex("name")
                    val name = students.getString(indexName)

                    startActivity(Intent(this@ScanActivity, MainActivity::class.java)
                        .putExtra("rfid", RFID_CARD)
                        .putExtra("name", name))

                    students.close()
                    dbHelper.close()
                } else if(employees != null && employees.moveToFirst()) {
                    val indexName = employees.getColumnIndex("name")
                    val name = employees.getString(indexName)

                    startActivity(Intent(this@ScanActivity, MainActivity::class.java)
                        .putExtra("rfid", RFID_CARD)
                        .putExtra("name", name))

                    employees.close()
                    dbHelper.close()
                } else{
                    Toast.makeText(this, "Kartu tidak terdaftar", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this@ScanActivity, "Harap Scan Kartu Anda", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun backService(){
        startActivity(Intent(this@ScanActivity, MenuActivity::class.java))
        finish()
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        backService()
    }
}