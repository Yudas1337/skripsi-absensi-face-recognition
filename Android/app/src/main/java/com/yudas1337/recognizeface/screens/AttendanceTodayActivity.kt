package com.yudas1337.recognizeface.screens

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import com.yudas1337.recognizeface.R
import com.yudas1337.recognizeface.adapter.AttendanceAdapter
import kotlinx.android.synthetic.main.activity_attendance_today.btn_bck
import kotlinx.android.synthetic.main.activity_attendance_today.recycler_view

class AttendanceTodayActivity : AppCompatActivity() {
    private var results: List<String> = listOf("yudas", "aku", "akbar", "tester", "femas", "arip", "ibnu");
    private var viewAdapter: AttendanceAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_attendance_today)

        val gridLayoutManager = GridLayoutManager(this,1)
        recycler_view!!.layoutManager = gridLayoutManager
        viewAdapter = AttendanceAdapter(this, results)
        recycler_view!!.adapter = viewAdapter

        btn_bck.setOnClickListener {
            startActivity(Intent(this, MenuActivity::class.java))
            finish()
        }
    }
}