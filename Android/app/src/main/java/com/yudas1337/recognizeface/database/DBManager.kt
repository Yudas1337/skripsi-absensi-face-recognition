package com.yudas1337.recognizeface.database

import android.content.Context
import android.content.SharedPreferences
import android.os.Handler
import android.util.Log
import com.yudas1337.recognizeface.helpers.AlertHelper
import com.yudas1337.recognizeface.network.Result
import com.yudas1337.recognizeface.screens.SyncActivity

class DBManager(private val dbHelper: DBHelper,
                private val context: Context,
                private val sharedPreferences: SharedPreferences?) {

    private var processedDataCount: Int = 0

    fun insertEmployeesFromJson(jsonData: List<Result>?){
        try {
            if (jsonData != null) {
                val totalData = SharedPref.getInt(sharedPreferences, "totalEmployees")
                processedDataCount = 0

                Log.d("percentage", "awalnya $processedDataCount")

                for (i in jsonData.indices) {
                    val data: Map<String, Any?> = mapOf(
                        "uuid" to jsonData[i].id,
                        "name" to jsonData[i].name,
                        "email" to jsonData[i].email,
                        "national_identity_number" to jsonData[i].national_identity_number,
                        "phone_number" to jsonData[i].phone_number,
                        "position" to jsonData[i].position,
                        "photo" to jsonData[i].photo,
                        "gender" to jsonData[i].gender,
                        "salary" to jsonData[i].salary,
                        "rfid" to jsonData[i].rfid,
                        "address" to jsonData[i].address,
                        "date_of_birth" to jsonData[i].date_of_birth,
                    )
                    dbHelper.insertData(Table.employees, data)

                    if(context is SyncActivity){
                        processedDataCount++
                        val progressPercent = (processedDataCount.toDouble() / totalData.toDouble() * 100).toInt()

                        context.runOnUiThread {
                            if(progressPercent == 100){
                                context.pDialog.dismissWithAnimation()
                                AlertHelper.successDialog(context, contentText = "Sinkronisasi Pengguna Berhasil")
                            } else{
                                context.pDialog.titleText = "Loading $progressPercent% of 100"
                            }
                        }
                    }
                }
            }
        }
        catch (e: Exception){
            e.printStackTrace()
        }
    }

     fun insertStudentsFromJson(jsonData: List<Result>?) {
        try {
            if (jsonData != null) {
                val totalData = SharedPref.getInt(sharedPreferences, "totalStudents")
                processedDataCount = 0

                for (i in jsonData.indices) {
                    val data: Map<String, Any?> = mapOf(
                        "id" to  jsonData[i].id,
                        "name" to jsonData[i].name,
                        "email" to jsonData[i].email,
                        "photo" to jsonData[i].photo,
                        "national_student_number" to jsonData[i].national_student_number,
                        "classroom" to jsonData[i].classroom,
                        "school" to jsonData[i].school,
                        "rfid" to jsonData[i].rfid,
                        "gender" to jsonData[i].gender,
                        "address" to jsonData[i].address,
                        "phone_number" to jsonData[i].phone_number,
                        "created_at" to jsonData[i].created_at,
                        "updated_at" to jsonData[i].updated_at,
                    )

                    dbHelper.insertData(Table.students, data)

                    if(context is SyncActivity){
                        processedDataCount++
                        val progressPercent = (processedDataCount.toDouble() / totalData.toDouble() * 100).toInt()

                        context.runOnUiThread {
                            if(progressPercent < 100){
                                context.pDialog.titleText = "Loading $progressPercent% of 100"
                            }
                        }
                    }
                }

            }
        }
        catch (e: Exception){
            e.printStackTrace()
        }
    }

    fun insertSchedulesFromJson(jsonData: List<Result>?){
        try {
            if (jsonData != null) {
                for (i in jsonData.indices) {
                    val data: Map<String, Any?> = mapOf(
                        "day" to jsonData[i].day,
                        "checkin_starts" to jsonData[i].checkin_starts,
                        "checkin_ends" to jsonData[i].checkin_ends,
                        "break_starts" to jsonData[i].break_starts,
                        "break_ends" to jsonData[i].break_ends,
                        "return_starts" to jsonData[i].return_starts,
                        "return_ends" to jsonData[i].return_ends,
                        "checkout_starts" to jsonData[i].checkout_starts,
                        "checkout_ends" to jsonData[i].checkout_ends
                    )
                    dbHelper.insertData(Table.schedules, data)
                }
            }
        }
        catch (e: Exception){
            e.printStackTrace()
        }
    }

    fun insertAttendanceLimitFromJson(jsonData: List<Result>?){
        try {
            if (jsonData != null) {
                for (i in jsonData.indices) {
                    val data: Map<String, Any?> = mapOf(
                        "minute" to jsonData[i].minute,
                    )
                    dbHelper.insertData(Table.attendance_rule, data)

                    if(context is SyncActivity){
                        context.pDialog.dismissWithAnimation()
                        AlertHelper.successDialog(context, contentText = "Sinkronisasi Jadwal Berhasil")
                    }

                }
            }
        }
        catch (e: Exception){
            e.printStackTrace()
        }
    }
}