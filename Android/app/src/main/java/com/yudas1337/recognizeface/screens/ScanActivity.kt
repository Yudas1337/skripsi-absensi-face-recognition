package com.yudas1337.recognizeface.screens

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.yudas1337.recognizeface.R
import com.yudas1337.recognizeface.database.DBHelper
import com.yudas1337.recognizeface.helpers.AlertHelper
import com.yudas1337.recognizeface.helpers.VoiceHelper
import com.yudas1337.recognizeface.services.ApiService
import com.yudas1337.recognizeface.services.ScanService
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class ScanActivity : AppCompatActivity() {

    private lateinit var relativeOne: RelativeLayout
    private lateinit var rfidController : EditText
    private lateinit var copyRight: TextView
    private lateinit var btnSubmit: Button
    private var voiceHelper: VoiceHelper? = null
    private lateinit var dbHelper: DBHelper

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan)

        relativeOne = findViewById(R.id.relative_1)
        copyRight = findViewById(R.id.cr_2)
        rfidController = findViewById(R.id.rfidController)
        btnSubmit = findViewById(R.id.btn_submit)

        voiceHelper = VoiceHelper.getInstance(this)
        voiceHelper!!.initializeTextToSpeech(this)

        relativeOne.setOnClickListener{
            backService()
        }

        dbHelper = DBHelper(this, null)

        val currentYear = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy"))
        val copyrightText = "$currentYear Hummatech All Rights Reserved."
        copyRight.text = copyrightText

        fetchData()

        btnSubmit.setOnClickListener {
            val RFID_CARD = rfidController.text.toString()
            if (RFID_CARD.isNotEmpty()) {
                val pDialog = AlertHelper.progressDialog(this, "Loading")
                pDialog.show()
                ScanService(this, dbHelper, voiceHelper!!).handleScan(RFID_CARD)
                pDialog.dismissWithAnimation()
                rfidController.text.clear()
            } else {
                AlertHelper.runVoiceAndToast(voiceHelper!!, this, "Harap Scan Kartu Anda")
            }
        }

    }

    private fun fetchData(){
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

    private fun backService(){
        startActivity(Intent(this@ScanActivity, MenuActivity::class.java))
        finish()
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        backService()
    }

    override fun onDestroy() {
        super.onDestroy()
        voiceHelper!!.stopAndShutdown()
    }
}