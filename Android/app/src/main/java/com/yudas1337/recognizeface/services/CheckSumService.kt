package com.yudas1337.recognizeface.services

import android.content.SharedPreferences
import android.util.Log
import com.yudas1337.recognizeface.constants.ConstShared
import com.yudas1337.recognizeface.database.SharedPref
import com.yudas1337.recognizeface.network.Value
import com.yudas1337.recognizeface.network.config.RetrofitBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CheckSumService(private val sharedPreferences: SharedPreferences) {
    fun checkAllSum(){

        studentSum()
        employeeSum()
        scheduleSum()
        limitSum()
        studentFaceSum()
        employeeFaceSum()
    }

    private fun studentSum(){
        val call: Call<Value> = RetrofitBuilder.builder().getStudents()

        call.enqueue(object : Callback<Value> {
            override fun onResponse(call: Call<Value>, response: Response<Value>) {
                if (response.isSuccessful) {
                    response.body()?.md5?.let{
                        SharedPref.putString(sharedPreferences, ConstShared.FETCH_MD5_STUDENTS, it)
                    }
                } else {
                    Log.d("connFailure", "Gagal siswa")
                }
            }

            override fun onFailure(call: Call<Value>, t: Throwable) {
                Log.d("connFailure", "Gagal siswa ${t.message}")
            }
        })
    }

    private fun employeeSum(){
        val call: Call<Value> = RetrofitBuilder.employeeBuilder().getEmployees()

        call.enqueue(object : Callback<Value> {
            override fun onResponse(call: Call<Value>, response: Response<Value>) {
                if (response.isSuccessful) {
                    response.body()?.md5?.let{
                        SharedPref.putString(sharedPreferences, ConstShared.FETCH_MD5_EMPLOYEES, it)
                    }
                } else {
                    Log.d("connFailure", "Gagal pegawai")
                }
            }

            override fun onFailure(call: Call<Value>, t: Throwable) {
                Log.d("connFailure", "Gagal pegawai ${t.message}")
            }
        })
    }

    private fun scheduleSum(){
        val call: Call<Value> = RetrofitBuilder.builder().getSchedules()

        call.enqueue(object : Callback<Value> {
            override fun onResponse(call: Call<Value>, response: Response<Value>) {
                if (response.isSuccessful) {
                    response.body()?.md5?.let{
                        SharedPref.putString(sharedPreferences, ConstShared.FETCH_MD5_SCHEDULES, it)
                    }
                } else {
                    Log.d("connFailure", "Gagal jadwal")
                }
            }

            override fun onFailure(call: Call<Value>, t: Throwable) {
                Log.d("connFailure", "Gagal jadwal ${t.message}")
            }
        })
    }

    private fun limitSum(){
        val call: Call<Value> = RetrofitBuilder.builder().getAttendanceLimit()

        call.enqueue(object : Callback<Value> {
            override fun onResponse(call: Call<Value>, response: Response<Value>) {
                if (response.isSuccessful) {
                    response.body()?.md5?.let{
                        SharedPref.putString(sharedPreferences, ConstShared.FETCH_MD5_LIMIT, it)
                    }
                } else {
                    Log.d("connFailure", "Gagal limit absensi")
                }
            }

            override fun onFailure(call: Call<Value>, t: Throwable) {
                Log.d("connFailure", "Gagal limit absensi ${t.message}")
            }
        })
    }

    private fun studentFaceSum(){
        val call: Call<Value> = RetrofitBuilder.builder().getStudentFaces()

        call.enqueue(object : Callback<Value> {
            override fun onResponse(call: Call<Value>, response: Response<Value>) {
                if (response.isSuccessful) {
                    response.body()?.md5?.let{
                        SharedPref.putString(sharedPreferences, ConstShared.FETCH_MD5_STUDENT_FACES, it)
                    }
                } else {
                    Log.d("connFailure", "Gagal Wajah Siswa")
                }
            }

            override fun onFailure(call: Call<Value>, t: Throwable) {
                Log.d("connFailure", "Gagal Wajah Siswa ${t.message}")
            }
        })
    }

    private fun employeeFaceSum(){
        val call = RetrofitBuilder.employeeBuilder().getEmployeeFaces()

        call.enqueue(object : Callback<Value> {
            override fun onResponse(call: Call<Value>, response: Response<Value>) {
                if (response.isSuccessful) {
                    response.body()?.md5?.let{
                        SharedPref.putString(sharedPreferences, ConstShared.FETCH_MD5_EMPLOYEE_FACES, it)
                    }
                } else {
                    Log.d("connFailure", "Gagal Wajah Pegawai")
                }
            }

            override fun onFailure(call: Call<Value>, t: Throwable) {
                Log.d("connFailure", "Gagal Wajah Pegawai ${t.message}")
            }
        })
    }

}