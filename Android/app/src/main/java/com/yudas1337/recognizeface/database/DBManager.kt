package com.yudas1337.recognizeface.database

import android.util.Log
import com.yudas1337.recognizeface.network.Result
import org.json.JSONArray
import org.json.JSONObject

class DBManager(private val dbHelper: DBHelper) {

    fun insertStudentsFromJson(jsonData: List<Result>?) {
        try {
            val jsonArray = JSONArray(jsonData)
            for (i in 0 until jsonArray.length()) {
                val jsonObject: JSONObject = jsonArray.getJSONObject(i)
                val data = mapOf(
                            "id" to  jsonObject.getInt("id"),
                            "name" to jsonObject.getString("name"),
                            "email" to jsonObject.getString("email"),
                            "photo" to jsonObject.getString("photo"),
                            "national_student_number" to jsonObject.getString("national_student_number"),
                            "classroom" to jsonObject.getInt("classroom"),
                            "school" to jsonObject.getString("school"),
                            "rfid" to jsonObject.getString("rfid"),
                            "gender" to jsonObject.getString("gender"),
                            "address" to jsonObject.getString("address"),
                            "phone_number" to jsonObject.getString("phone_number"),
                            "created_at" to jsonObject.getString("created_at"),
                            "updated_at" to jsonObject.getString("updated_at"),
                        )
                dbHelper.insertData(Table.students, data)
            }
        }
        catch (e: Exception){
            e.printStackTrace()
        }
    }

    fun insertSchedulesFromJson(jsonData: List<Result>?){
        try {
            val jsonArray = JSONArray(jsonData)
            for (i in 0 until jsonArray.length()) {
                val jsonObject: JSONObject = jsonArray.getJSONObject(i)
                val data = mapOf(
                    "day" to jsonObject.getString("day"),
                    "checkin_starts" to jsonObject.getString("checkin_starts"),
                    "checkin_ends" to jsonObject.getString("checkin_ends"),
                    "break_starts" to jsonObject.getString("break_starts"),
                    "break_ends" to jsonObject.getString("break_ends"),
                    "return_starts" to jsonObject.getString("return_starts"),
                    "return_ends" to jsonObject.getString("return_ends"),
                    "checkout_starts" to jsonObject.getString("checkout_starts"),
                    "checkout_ends" to jsonObject.getString("checkout_ends")
                )
                dbHelper.insertData(Table.schedules, data)
            }

        }
        catch (e: Exception){
            e.printStackTrace()
        }
    }



}