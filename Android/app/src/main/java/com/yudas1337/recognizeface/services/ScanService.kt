package com.yudas1337.recognizeface.services

import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.yudas1337.recognizeface.database.DBHelper
import com.yudas1337.recognizeface.screens.MainActivity

class ScanService(private val context: Context) {
    fun handleScan(RFID_CARD: String) {
        val dbHelper = DBHelper(context, null)
        val students = dbHelper.findUsersByRfid("students", RFID_CARD)
        val employees = dbHelper.findUsersByRfid("employees", RFID_CARD)


        if (students != null && students.moveToFirst()) {
            val indexName = students.getColumnIndex("name")
            val name = students.getString(indexName)

            startActivity(context, RFID_CARD, name)

            students.close()
            dbHelper.close()
        } else if(employees != null && employees.moveToFirst()) {
            val indexName = employees.getColumnIndex("name")
            val name = employees.getString(indexName)

            startActivity(context, RFID_CARD, name)

            employees.close()
            dbHelper.close()
        } else{
            Toast.makeText(context, "Kartu tidak terdaftar", Toast.LENGTH_SHORT).show()
        }
    }

    private fun startActivity(context: Context, RFID_CARD: String, name: String) {
        context.startActivity(
            Intent(context, MainActivity::class.java)
            .putExtra("rfid", RFID_CARD)
            .putExtra("name", name))
    }
}