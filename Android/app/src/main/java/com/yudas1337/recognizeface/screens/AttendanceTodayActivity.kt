package com.yudas1337.recognizeface.screens

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import androidx.annotation.RequiresApi
import androidx.core.database.getStringOrNull
import androidx.recyclerview.widget.GridLayoutManager
import com.yudas1337.recognizeface.R
import com.yudas1337.recognizeface.adapter.AttendanceAdapter
import com.yudas1337.recognizeface.database.AttendanceData
import com.yudas1337.recognizeface.database.DBHelper
import kotlinx.android.synthetic.main.activity_attendance_today.btn_bck
import kotlinx.android.synthetic.main.activity_attendance_today.recycler_view

class AttendanceTodayActivity : AppCompatActivity() {
    private var viewAdapter: AttendanceAdapter? = null
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_attendance_today)

        val gridLayoutManager = GridLayoutManager(this,1)
        recycler_view!!.layoutManager = gridLayoutManager
        viewAdapter = AttendanceAdapter(this, fetchTodayAttendance())
        recycler_view!!.adapter = viewAdapter

        val btnEmployee = findViewById<LinearLayout>(R.id.buttonEmployee)
        btnEmployee.setOnClickListener {
            startActivity(Intent(this, EmployeeAttendanceActivity::class.java))
            finish()
        }

        btn_bck.setOnClickListener {
            startActivity(Intent(this, MenuActivity::class.java))
            finish()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun fetchTodayAttendance(): List<AttendanceData> {

        val dbHelper = DBHelper(this, null)
        val cursor = dbHelper.fetchStudentAttendances("")
        val attendances = mutableListOf<AttendanceData>()

        while (cursor.moveToNext()) {
            val name = cursor.getStringOrNull(cursor.getColumnIndex("name")) ?: ""
            val status = cursor.getStringOrNull(cursor.getColumnIndex("status")) ?: ""
            val school = cursor.getStringOrNull(cursor.getColumnIndex("school")) ?: ""
            val present = cursor.getStringOrNull(cursor.getColumnIndex("present_hour")) ?: ""
            val breakTime = cursor.getStringOrNull(cursor.getColumnIndex("break_hour")) ?: ""
            val returnBreak = cursor.getStringOrNull(cursor.getColumnIndex("return_break_hour")) ?: ""
            val returnHome = cursor.getStringOrNull(cursor.getColumnIndex("return_hour")) ?: ""

            val attendance = AttendanceData(
                name = name,
                status = status,
                school = school,
                present = present,
                breakTime = breakTime,
                returnBreak = returnBreak,
                returnHome = returnHome
            )
            attendances.add(attendance)
        }

        return attendances
    }
}