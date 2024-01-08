package com.yudas1337.recognizeface.screens

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.yudas1337.recognizeface.R
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

        copyRight = findViewById(R.id.cr_2)
        rfidController = findViewById(R.id.rfidController)
        btnSubmit = findViewById(R.id.btn_submit)
        val currentYear = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy"))
        val copyrightText = "$currentYear Hummatech All Rights Reserved."
        copyRight.text = copyrightText

        btnSubmit.setOnClickListener {
            val RFID_CARD = rfidController.text.toString()
            if (RFID_CARD.isNotEmpty()) {
                Toast.makeText(this@ScanActivity, "Melakukan scan ID Card: $RFID_CARD", Toast.LENGTH_SHORT).show()
                val intent = Intent(this@ScanActivity, MainActivity::class.java)
                    .putExtra("RFID_CARD", RFID_CARD)
                startActivity(intent)
            } else {
                Toast.makeText(this@ScanActivity, "Harap isi ID Card Anda", Toast.LENGTH_SHORT).show()
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