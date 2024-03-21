package com.yudas1337.recognizeface.screens

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.GridLayoutManager
import com.yudas1337.recognizeface.R
import com.yudas1337.recognizeface.adapter.EmployeeAdapter
import com.yudas1337.recognizeface.database.DBHelper
import com.yudas1337.recognizeface.network.Result
import kotlinx.android.synthetic.main.activity_employee_list.btnStudent
import kotlinx.android.synthetic.main.activity_employee_list.btn_bck
import kotlinx.android.synthetic.main.activity_employee_list.recyclermodules
import kotlinx.android.synthetic.main.activity_employee_list.searchButton
import kotlinx.android.synthetic.main.activity_employee_list.searchText

class EmployeeListActivity : AppCompatActivity() {

    private var viewAdapter: EmployeeAdapter? = null
    private var searchName: String = ""
    private lateinit var keyboardPopUp: InputMethodManager
    private lateinit var dbHelper: DBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_employee_list)

        keyboardPopUp = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        dbHelper = DBHelper(this, null)

        val grdLayoutManager = GridLayoutManager(this,2)
        recyclermodules!!.layoutManager = grdLayoutManager

        prepareData(searchName)

        btnStudent.setOnClickListener {
            startActivity(Intent(this, UserListActivity::class.java))
            finish()
        }

        btn_bck.setOnClickListener {
            startActivity(Intent(this, MenuActivity::class.java))
            finish()
        }

        searchButton.setOnClickListener{
            searchUser(searchText.query.toString())
            keyboardPopUp.hideSoftInputFromWindow(searchText.windowToken, 0)
        }

        searchText.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(p0: String?): Boolean {
                p0?.let {
                    searchUser(it)
                }
                keyboardPopUp.hideSoftInputFromWindow(searchText.windowToken, 0)
                return true
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                p0?.let {
                    searchUser(it)
                }
                return true
            }
        })

        val searchCloseButtonId = searchText.findViewById<View>(androidx.appcompat.R.id.search_close_btn).id
        val closeButton = searchText.findViewById<ImageView>(searchCloseButtonId)
        closeButton.setOnClickListener {
            searchText.setQuery("", false)
            searchText.clearFocus()
            prepareData(searchName)
            keyboardPopUp.hideSoftInputFromWindow(searchText.windowToken, 0)
        }
    }

    private fun prepareData(search: String){
        val dataList = mutableListOf<Result>()
        val cursor = dbHelper.getEmployees(search)

        cursor?.use {
            if (it.moveToFirst()) {
                val idColumnIndex = it.getColumnIndex("id")
                val nameColumnIndex = it.getColumnIndex("name")
                val emailColumnIndex = it.getColumnIndex("email")
                val photoColumnIndex = it.getColumnIndex("photo")
                val nationalIdentityNumberColumnIndex = it.getColumnIndex("national_identity_number")
                val positionColumnIndex = it.getColumnIndex("position")

                do {
                    if (idColumnIndex >= 0 && nameColumnIndex >= 0) {
                        val a = Result()
                        a.id = it.getString(idColumnIndex)
                        a.name = it.getString(nameColumnIndex)
                        a.email = it.getString(emailColumnIndex)
                        a.photo = it.getString(photoColumnIndex)
                        a.position = it.getString(positionColumnIndex)
                        a.national_identity_number = it.getString(nationalIdentityNumberColumnIndex)
                        dataList.add(a)
                    }
                } while (it.moveToNext())
            }
        }

        viewAdapter = EmployeeAdapter(dataList)
        recyclermodules!!.adapter = viewAdapter
    }

    private fun searchUser(search: String){
        if(search.isNotEmpty()){
            prepareData(search)
        } else{
            prepareData(searchName)
        }
    }
}