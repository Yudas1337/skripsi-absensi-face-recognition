package com.yudas1337.recognizeface.network

import com.yudas1337.recognizeface.constants.URL
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface Request {
    @Multipart
    @POST("attendance-rfid/")
    fun doAttendance(
        @Part image: MultipartBody.Part,
        rfid: String
    ): Call<Value>
}