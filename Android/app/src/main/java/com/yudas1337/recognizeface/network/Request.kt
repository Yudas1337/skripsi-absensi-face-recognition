package com.yudas1337.recognizeface.network

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Url

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
    @GET("face")
    fun getEmployeeFaces(): Call<Value>

    @GET
    suspend fun downloadImage(@Url imageUrl: String): Response<ResponseBody>
}