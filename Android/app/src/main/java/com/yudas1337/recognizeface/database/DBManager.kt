package com.yudas1337.recognizeface.database

import android.util.Log
import com.yudas1337.recognizeface.network.Result
import org.json.JSONArray
import org.json.JSONObject

class DBManager(private val dbHelper: DBHelper) {

    fun insertStudentsFromJson(jsonData: List<Result>?) {
        try {
            if (jsonData != null) {
                for (i in jsonData.indices) {
                    val data: Map<String, Any?> = mapOf(
                        "id" to  jsonData[i].id?.toInt(),
                        "name" to jsonData[i].name,
                        "email" to jsonData[i].email,
                        "photo" to jsonData[i].photo,
                        "national_student_number" to jsonData[i].national_student_number,
                        "classroom" to jsonData[i].classroom?.toInt(),
                        "school" to jsonData[i].school,
                        "rfid" to jsonData[i].rfid,
                        "gender" to jsonData[i].gender,
                        "address" to jsonData[i].address,
                        "phone_number" to jsonData[i].phone_number,
                        "created_at" to jsonData[i].created_at,
                        "updated_at" to jsonData[i].updated_at,
                    )
                    dbHelper.insertData(Table.students, data)
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
}