package com.yudas1337.recognizeface.network

import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface Request {
    @Multipart
    @POST("/predict")
    fun recognizeFaceRequest(
        @Part image: MultipartBody.Part
    ): Call<Value>
}