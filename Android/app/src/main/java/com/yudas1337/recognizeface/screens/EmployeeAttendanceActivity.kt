package com.yudas1337.recognizeface.screens

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import androidx.recyclerview.widget.GridLayoutManager
import com.yudas1337.recognizeface.R
import com.yudas1337.recognizeface.adapter.AttendanceAdapter
import com.yudas1337.recognizeface.adapter.EmployeeAttendanceAdapter
import kotlinx.android.synthetic.main.activity_attendance_today.recycler_view

class EmployeeAttendanceActivity : AppCompatActivity() {
    private var results: List<String> = listOf("SMKN 1 KEPANJEN", "SMKN 1 LUMAJANG", "SMK NEGERI 1 LUAMAJANG", "tester", "femas", "arip", "ibnu");
    private var viewAdapter: EmployeeAttendanceAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_employee_attendance)

        val gridLayoutManager = GridLayoutManager(this,1)
        recycler_view!!.layoutManager = gridLayoutManager

        viewAdapter = EmployeeAttendanceAdapter(this, results)
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
}