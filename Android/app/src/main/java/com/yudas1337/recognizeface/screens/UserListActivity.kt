package com.yudas1337.recognizeface.screens

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.yudas1337.recognizeface.R
import com.yudas1337.recognizeface.adapter.StudentAdapter
import com.yudas1337.recognizeface.database.DBHelper
import com.yudas1337.recognizeface.network.Result
import kotlinx.android.synthetic.main.activity_user_list.recyclermodules

class UserListActivity : AppCompatActivity() {

    private var results: List<Result>? = ArrayList()
    private var viewAdapter: StudentAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_list)

        val dbHelper = DBHelper(this, null)

        val dataList = mutableListOf<Result>()
        val cursor = dbHelper.getStudents()
        cursor?.use {
            if (it.moveToFirst()) {
                val idColumnIndex = it.getColumnIndex("id")
                val nameColumnIndex = it.getColumnIndex("name")
                val emailColumnIndex = it.getColumnIndex("email")
                val photoColumnIndex = it.getColumnIndex("photo")
                val schoolColumnIndex = it.getColumnIndex("school")

                do {
                    if (idColumnIndex >= 0 && nameColumnIndex >= 0) {
                        val a = Result()
                        a.id = it.getString(idColumnIndex)
                        a.name = it.getString(nameColumnIndex)
                        a.email = it.getString(emailColumnIndex)
                        a.photo = it.getString(photoColumnIndex)
                        a.school = it.getString(schoolColumnIndex)
                        dataList.add(a)
                    }
                } while (it.moveToNext())
            }
        }

        val grdLayoutManager = GridLayoutManager(this,2)
        recyclermodules!!.layoutManager = grdLayoutManager
        results = dataList
        viewAdapter = results?.let { StudentAdapter(this, it) }
        recyclermodules!!.adapter = viewAdapter

        val buttonEmployee = findViewById<LinearLayout>(R.id.buttonEmployee)
        buttonEmployee.setOnClickListener {
            startActivity(Intent(this, EmployeeListActivity::class.java))
            finish()
        }

        val buttonBack = findViewById<Button>(R.id.btn_bck)

        buttonBack.setOnClickListener {
            startActivity(Intent(this, MenuActivity::class.java))
            finish()
        }

    }
}