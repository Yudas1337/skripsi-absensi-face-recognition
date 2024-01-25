package com.yudas1337.recognizeface.services

import android.content.Context
import android.util.Log
import com.yudas1337.recognizeface.database.DBHelper
import com.yudas1337.recognizeface.database.DBManager
import com.yudas1337.recognizeface.network.Value
import com.yudas1337.recognizeface.network.config.RetrofitBuilder
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class ApiService(private val context: Context) {

    fun getStudents(){
        val call: Call<Value> = RetrofitBuilder.builder(context).getStudents()

        call.enqueue(object : Callback<Value> {
            override fun onResponse(call: Call<Value>, response: Response<Value>) {
                if (response.isSuccessful) {
                    val responseData = response.body()?.result
                    val dbHelper = DBHelper(context, null)
                    DBManager(dbHelper).insertStudentsFromJson(responseData)
                } else {
                    Log.d("connFailure", "Gagal")
                }
            }

            override fun onFailure(call: Call<Value>, t: Throwable) {
                Log.d("connFailure", "Gagal")
            }
        })
    }

    fun getSchedules(){
        val call: Call<Value> = RetrofitBuilder.builder(context).getSchedules()

        call.enqueue(object : Callback<Value> {
            override fun onResponse(call: Call<Value>, response: Response<Value>) {
                if (response.isSuccessful) {
                    val responseData = response.body()?.result
                    val dbHelper = DBHelper(context, null)
                    DBManager(dbHelper).insertSchedulesFromJson(responseData)
                } else {
                    Log.d("connFailure", "Gagal")
                }
            }

            override fun onFailure(call: Call<Value>, t: Throwable) {
                Log.d("connFailure", "Gagal")
            }
        })

    }

    fun recognizeFaceApi(imageFile: File) {
        // Create a request body with the file and content type
        val requestFile: RequestBody =
            RequestBody.create(MediaType.parse("multipart/form-data"), imageFile)

        // Create MultipartBody.Part using file request-body, name, and filename
        val body: MultipartBody.Part =
            MultipartBody.Part.createFormData("image", imageFile.name, requestFile)
    }

}