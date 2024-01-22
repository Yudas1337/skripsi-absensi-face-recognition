package com.yudas1337.recognizeface.database

import org.json.JSONArray
import org.json.JSONObject

class DBManager(private val dbHelper: DBHelper) {
    fun insertStudentsFromJson(jsonData: String) {
        try {
            val jsonArray = JSONArray(jsonData)
            for (i in 0 until jsonArray.length()) {
                val jsonObject: JSONObject = jsonArray.getJSONObject(i)
                val data = mapOf(
                            "id" to  jsonObject.getInt("id"),
                            "name" to jsonObject.getString("name"),
                            "photo" to jsonObject.getString("photo"),
                            "national_student_number" to jsonObject.getString("national_student_number"),
                            "classroom" to jsonObject.getInt("classroom"),
                            "gender" to jsonObject.getString("gender"),
                            "address" to jsonObject.getString("address"),
                            "created_at" to jsonObject.getString("created_at"),
                            "updated_at" to jsonObject.getString("updated_at"),
                        )
                dbHelper.insertData(Table.students, data)
            }

        }
        catch (e: Exception){
                    e.printStackTrace()
        }
        finally {
            dbHelper.close()
        }
    }



}