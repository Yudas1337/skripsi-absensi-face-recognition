package com.yudas1337.recognizeface.network

import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface Request {
    @Multipart
    @POST("attendance-rfid/")
    fun uploadSync(
        @Part image: MultipartBody.Part,
        rfid: String
    ): Call<Value>

    @GET("students")
    fun getStudents(): Call<Value>

    @GET("entry-time")
    fun getSchedules(): Call<Value>

    @GET("employees")
    fun getEmployees(): Call<Value>

    @GET("limit")
    fun getAttendanceLimit(): Call<Value>
}