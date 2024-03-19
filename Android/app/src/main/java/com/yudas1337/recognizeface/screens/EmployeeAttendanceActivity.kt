package com.yudas1337.recognizeface.screens

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import androidx.annotation.RequiresApi
import androidx.core.database.getStringOrNull
import androidx.recyclerview.widget.GridLayoutManager
import com.yudas1337.recognizeface.R
import com.yudas1337.recognizeface.adapter.EmployeeAttendanceAdapter
import com.yudas1337.recognizeface.database.AttendanceData
import com.yudas1337.recognizeface.database.DBHelper
import kotlinx.android.synthetic.main.activity_attendance_today.recycler_view

class EmployeeAttendanceActivity : AppCompatActivity() {
    private var viewAdapter: EmployeeAttendanceAdapter? = null
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_employee_attendance)

        val gridLayoutManager = GridLayoutManager(this,1)
        recycler_view!!.layoutManager = gridLayoutManager

        viewAdapter = EmployeeAttendanceAdapter(DBHelper(this, null),fetchTodayAttendance())
        recycler_view!!.adapter = viewAdapter

        val btnStudent = findViewById<LinearLayout>(R.id.tabStudent);
        btnStudent.setOnClickListener {
            startActivity(Intent(this, AttendanceTodayActivity::class.java))
            finish()
        }

        val buttonBack = findViewById<Button>(R.id.btn_bck)

        buttonBack.setOnClickListener {
            startActivity(Intent(this, MenuActivity::class.java))
            finish()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun fetchTodayAttendance(): List<AttendanceData> {

        val dbHelper = DBHelper(this, null)
        val cursor = dbHelper.fetchEmployeeAttendances("")
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