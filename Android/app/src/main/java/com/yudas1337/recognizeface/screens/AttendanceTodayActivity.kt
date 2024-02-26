package com.yudas1337.recognizeface.screens

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import com.yudas1337.recognizeface.R
import com.yudas1337.recognizeface.adapter.AttendanceAdapter
import kotlinx.android.synthetic.main.activity_attendance_today.resikelpiew

class AttendanceTodayActivity : AppCompatActivity() {
    private var results: List<String> = listOf("halo", "aku", "akbar");
    private var viewAdapter: AttendanceAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_attendance_today)

        val grdLayoutManager = GridLayoutManager(this,1)
        resikelpiew!!.layoutManager = grdLayoutManager
        viewAdapter = results?.let { AttendanceAdapter(this, it) }
        resikelpiew!!.adapter = viewAdapter
    }
}