package com.yudas1337.recognizeface.services

import android.content.Context
import android.util.Log
import com.yudas1337.recognizeface.constants.ConstShared
import com.yudas1337.recognizeface.database.DBHelper
import com.yudas1337.recognizeface.database.DBManager
import com.yudas1337.recognizeface.database.SharedPref
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
        val call = RetrofitBuilder.builder().getStudents()
        call.enqueue(object : Callback<Value> {
            override fun onResponse(call: Call<Value>, response: Response<Value>) {
                if (response.isSuccessful) {
                    val responseData = response.body()?.result
                    val dbHelper = DBHelper(context, null)

                    val sharedPreferences = context.getSharedPreferences(ConstShared.fileName, Context.MODE_PRIVATE)
                    response.body()?.total?.let {
                        SharedPref.putInt(sharedPreferences, ConstShared.TOTAL_STUDENTS, it)
                    }

                    DBManager(dbHelper, context, sharedPreferences).insertStudentsFromJson(responseData)
                } else {
                    Log.e("connFailure", "Gagal siswa")
                }
            }

            override fun onFailure(call: Call<Value>, t: Throwable) {
                Log.e("connFailure", "Gagal siswa ${t.message}")
            }
        })
    }

    fun getEmployees() {
        val call: Call<Value> = RetrofitBuilder.employeeBuilder().getEmployees()

        call.enqueue(object : Callback<Value> {
            override fun onResponse(call: Call<Value>, response: Response<Value>) {
                if (response.isSuccessful) {
                    val responseData = response.body()?.result
                    val dbHelper = DBHelper(context, null)

                    val sharedPreferences = context.getSharedPreferences(ConstShared.fileName, Context.MODE_PRIVATE)
                    response.body()?.total?.let {
                        SharedPref.putInt(sharedPreferences, ConstShared.TOTAL_EMPLOYEES, it)
                    }

                    DBManager(dbHelper, context, sharedPreferences).insertEmployeesFromJson(responseData)
                } else {
                    Log.d("connFailure", "Gagal pegawai")
                }
            }

            override fun onFailure(call: Call<Value>, t: Throwable) {
                Log.d("connFailure", "Gagal pegawai ${t.message}")
            }
        })
    }

    fun getSchedules(){
        val call: Call<Value> = RetrofitBuilder.builder().getSchedules()

        call.enqueue(object : Callback<Value> {
            override fun onResponse(call: Call<Value>, response: Response<Value>) {
                if (response.isSuccessful) {
                    val responseData = response.body()?.result
                    val dbHelper = DBHelper(context, null)

                    val sharedPreferences = context.getSharedPreferences(ConstShared.fileName, Context.MODE_PRIVATE)
                    response.body()?.total?.let {
                        SharedPref.putInt(sharedPreferences, ConstShared.TOTAL_SCHEDULES, it)
                    }

                    DBManager(dbHelper, context, sharedPreferences).insertSchedulesFromJson(responseData)
                } else {
                    Log.d("connFailure", "Gagal jadwal")
                }
            }

            override fun onFailure(call: Call<Value>, t: Throwable) {
                Log.d("connFailure", "Gagal jadwal ${t.message}")
            }
        })

    }

    fun getAttendanceLimit(){
        val call: Call<Value> = RetrofitBuilder.builder().getAttendanceLimit()

        call.enqueue(object : Callback<Value> {
            override fun onResponse(call: Call<Value>, response: Response<Value>) {
                if (response.isSuccessful) {
                    val responseData = response.body()?.result
                    val dbHelper = DBHelper(context, null)

                    val sharedPreferences = context.getSharedPreferences(ConstShared.fileName, Context.MODE_PRIVATE)
                    response.body()?.total?.let {
                        SharedPref.putInt(sharedPreferences, ConstShared.TOTAL_LIMIT, it)
                    }

                    DBManager(dbHelper, context, sharedPreferences).insertAttendanceLimitFromJson(responseData)
                } else {
                    Log.d("connFailure", "Gagal limit absensi")
                }
            }

            override fun onFailure(call: Call<Value>, t: Throwable) {
                Log.d("connFailure", "Gagal limit absensi ${t.message}")
            }
        })

    }

    fun syncAttendances(){

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