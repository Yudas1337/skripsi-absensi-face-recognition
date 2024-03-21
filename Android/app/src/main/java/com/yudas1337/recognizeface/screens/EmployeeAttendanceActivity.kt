package com.yudas1337.recognizeface.screens

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.SearchView
import androidx.core.database.getStringOrNull
import androidx.recyclerview.widget.GridLayoutManager
import com.yudas1337.recognizeface.R
import com.yudas1337.recognizeface.adapter.AttendanceAdapter
import com.yudas1337.recognizeface.adapter.EmployeeAttendanceAdapter
import com.yudas1337.recognizeface.database.AttendanceData
import com.yudas1337.recognizeface.database.DBHelper
import kotlinx.android.synthetic.main.activity_attendance_today.recycler_view
import kotlinx.android.synthetic.main.activity_attendance_today.searchButton
import kotlinx.android.synthetic.main.activity_attendance_today.searchText
import kotlinx.android.synthetic.main.activity_employee_attendance.btn_bck

class EmployeeAttendanceActivity : AppCompatActivity() {
    private var viewAdapter: EmployeeAttendanceAdapter? = null
    private var searchName: String = ""
    private lateinit var keyboardPopUp: InputMethodManager

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_employee_attendance)

        keyboardPopUp = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        val gridLayoutManager = GridLayoutManager(this,1)

        recycler_view!!.layoutManager = gridLayoutManager
        viewAdapter = EmployeeAttendanceAdapter(DBHelper(this, null),fetchTodayAttendance(searchName))
        recycler_view!!.adapter = viewAdapter

        val btnStudent = findViewById<LinearLayout>(R.id.tabStudent);
        btnStudent.setOnClickListener {
            startActivity(Intent(this, AttendanceTodayActivity::class.java))
            finish()
        }

        btn_bck.setOnClickListener {
            startActivity(Intent(this, MenuActivity::class.java))
            finish()
        }

        searchButton.setOnClickListener{
            searchAttendance(searchText.query.toString())
            keyboardPopUp.hideSoftInputFromWindow(searchText.windowToken, 0)
        }

        searchText.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(p0: String?): Boolean {
                p0?.let {
                    searchAttendance(it)
                }
                keyboardPopUp.hideSoftInputFromWindow(searchText.windowToken, 0)
                return true
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                p0?.let {
                    searchAttendance(it)
                }
                return true
            }
        })


        val searchCloseButtonId = searchText.findViewById<View>(androidx.appcompat.R.id.search_close_btn).id
        val closeButton = searchText.findViewById<ImageView>(searchCloseButtonId)
        closeButton.setOnClickListener {
            searchText.setQuery("", false)
            searchText.clearFocus()
            viewAdapter = EmployeeAttendanceAdapter(DBHelper(this, null), fetchTodayAttendance(searchName))
            recycler_view!!.adapter = viewAdapter
            keyboardPopUp.hideSoftInputFromWindow(searchText.windowToken, 0)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun searchAttendance(search: String){
        if(search.isNotEmpty()){
            viewAdapter = EmployeeAttendanceAdapter(DBHelper(this, null), fetchTodayAttendance(search))
            recycler_view!!.adapter = viewAdapter
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun fetchTodayAttendance(searchName: String): List<AttendanceData> {

        val dbHelper = DBHelper(this, null)
        val cursor = dbHelper.fetchEmployeeAttendances(searchName)
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