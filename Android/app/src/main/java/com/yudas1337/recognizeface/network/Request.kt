package com.yudas1337.recognizeface.network

import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface Request {
    @POST("sync")
    fun syncAttendances(attendances: ArrayList<String>): Call<Value>

    @GET("students")
    fun getStudents(): Call<Value>

    @GET("entry-time")
    fun getSchedules(): Call<Value>

    @GET("employees")
    fun getEmployees(): Call<Value>

    @GET("limit")
    fun getAttendanceLimit(): Call<Value>

    // students
    @GET("face")
    fun getStudentFaces(): Call<Value>

    // employees
    @GET("face-api")
    fun getEmployeeFaces(): Call<Value>
}