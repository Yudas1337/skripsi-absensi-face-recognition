package com.yudas1337.recognizeface.services

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import com.yudas1337.recognizeface.constants.ConstShared
import com.yudas1337.recognizeface.constants.FaceFolder
import com.yudas1337.recognizeface.database.DBHelper
import com.yudas1337.recognizeface.database.DBManager
import com.yudas1337.recognizeface.database.SharedPref
import com.yudas1337.recognizeface.network.Request
import com.yudas1337.recognizeface.network.Value
import com.yudas1337.recognizeface.network.config.RetrofitBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class ApiService(private val context: Context) {

    fun getStudents(){
        val call = RetrofitBuilder.builder().getStudents()

        CoroutineScope(Dispatchers.Main).launch {
            try {
                val response = call.await()
                val responseData = response.result
                val dbHelper = DBHelper(context, null)
                val sharedPreferences = context.getSharedPreferences(ConstShared.fileName, Context.MODE_PRIVATE)

                response.total?.let {
                    SharedPref.putInt(sharedPreferences, ConstShared.TOTAL_STUDENTS, it)
                }

                withContext(Dispatchers.IO) {
                    DBManager(dbHelper, context, sharedPreferences).insertStudentsFromJson(responseData)
                }

            } catch (e: Exception) {
                Log.d("wajahnya", "Gagal siswa magang ${e.message}")
            }
        }
    }

    fun getEmployees() {
        val call: Call<Value> = RetrofitBuilder.employeeBuilder().getEmployees()

        CoroutineScope(Dispatchers.Main).launch {
            try {
                val response = call.await()
                val responseData = response.result
                val dbHelper = DBHelper(context, null)
                val sharedPreferences = context.getSharedPreferences(ConstShared.fileName, Context.MODE_PRIVATE)

                response.total?.let {
                    SharedPref.putInt(sharedPreferences, ConstShared.TOTAL_EMPLOYEES, it)
                }

                withContext(Dispatchers.IO) {
                    DBManager(dbHelper, context, sharedPreferences).insertEmployeesFromJson(responseData)
                }

            } catch (e: Exception) {
                Log.d("wajahnya", "Gagal pegawai ${e.message}")
            }
        }
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

    @OptIn(ExperimentalCoroutinesApi::class)
    suspend fun <T> Call<T>.await(): T {
        return suspendCancellableCoroutine { continuation ->
            enqueue(object : Callback<T> {
                override fun onResponse(call: Call<T>, response: Response<T>) {
                    if (response.isSuccessful) {
                        continuation.resume(response.body()!!)
                    } else {
                        continuation.resumeWithException(Exception("Failed to execute call: ${response.code()}"))
                    }
                }

                override fun onFailure(call: Call<T>, t: Throwable) {
                    continuation.resumeWithException(t)
                }
            })

            continuation.invokeOnCancellation {
                cancel()
            }
        }
    }

    fun getEmployeeFaces(){
        val call: Call<Value> = RetrofitBuilder.employeeBuilder().getEmployeeFaces()

        CoroutineScope(Dispatchers.Main).launch {
            try {
                val response = call.await()
                val responseData = response.result
                val dbHelper = DBHelper(context, null)
                val sharedPreferences = context.getSharedPreferences(ConstShared.fileName, Context.MODE_PRIVATE)

                withContext(Dispatchers.IO) {
                    DBManager(dbHelper, context, sharedPreferences).insertEmployeeFacesFromJson(responseData)
                }

            } catch (e: Exception) {
                Log.d("wajahnya", "Gagal wajah pegawai ${e.message}")
            }
        }
    }

    fun getStudentFaces(){
        val call: Call<Value> = RetrofitBuilder.builder().getStudentFaces()

        CoroutineScope(Dispatchers.Main).launch {
            try {
                val response = call.await()
                val responseData = response.result
                val dbHelper = DBHelper(context, null)
                val sharedPreferences = context.getSharedPreferences(ConstShared.fileName, Context.MODE_PRIVATE)

                withContext(Dispatchers.IO) {
                    DBManager(dbHelper, context, sharedPreferences).insertStudentFacesFromJson(responseData)
                }

            } catch (e: Exception) {
                Log.d("wajahnya", "Gagal wajah siswa ${e.message}")
            }
        }
    }
}