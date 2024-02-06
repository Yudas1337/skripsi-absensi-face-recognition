package com.yudas1337.recognizeface.screens

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.recyclerview.widget.GridLayoutManager
import com.yudas1337.recognizeface.R
import com.yudas1337.recognizeface.adapter.EmployeeAdapter
import com.yudas1337.recognizeface.database.DBHelper
import com.yudas1337.recognizeface.databinding.ActivityEmployeeListBinding
import com.yudas1337.recognizeface.network.Result
import kotlinx.android.synthetic.main.activity_user_list.recyclermodules

class EmployeeListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEmployeeListBinding
    private var results: List<Result>? = ArrayList()
    private var viewAdapter: EmployeeAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEmployeeListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val dbHelper = DBHelper(this, null)
        val dataList = mutableListOf<Result>()
        val cursor = dbHelper.getEmployees();
        cursor?.use {
            if (it.moveToFirst()) {
                val idColumnIndex = it.getColumnIndex("id")
                val nameColumnIndex = it.getColumnIndex("name")
                val emailColumnIndex = it.getColumnIndex("email")
                val photoColumnIndex = it.getColumnIndex("photo")
                val positionColumnIndex = it.getColumnIndex("position")

                do {
                    if (idColumnIndex >= 0 && nameColumnIndex >= 0) {
                        val a = Result()
                        a.id = it.getString(idColumnIndex)
                        a.name = it.getString(nameColumnIndex)
                        a.email = it.getString(emailColumnIndex)
                        a.photo = it.getString(photoColumnIndex)
                        a.position = it.getString(positionColumnIndex)
                        val myData = a;
                        dataList.add(myData)
                    }
                } while (it.moveToNext())
            }
        }

        val grdLayoutManager = GridLayoutManager(this,2)
        recyclermodules!!.layoutManager = grdLayoutManager
        results = dataList
        viewAdapter = results?.let { EmployeeAdapter(this, it) }
        recyclermodules!!.adapter = viewAdapter


        val buttonBack = findViewById<Button>(R.id.btn_bck)

        buttonBack.setOnClickListener {
            startActivity(Intent(this, MenuActivity::class.java))
            finish()
        }
    }
}