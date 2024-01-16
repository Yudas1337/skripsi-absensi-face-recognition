package com.yudas1337.recognizeface.services

import android.content.Context
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

    fun recognizeFaceApi(imageFile: File) {
        // Create a request body with the file and content type
        val requestFile: RequestBody =
            RequestBody.create(MediaType.parse("multipart/form-data"), imageFile)

        // Create MultipartBody.Part using file request-body, name, and filename
        val body: MultipartBody.Part =
            MultipartBody.Part.createFormData("image", imageFile.name, requestFile)

        // Call the uploadImage method from the ApiService interface
        val call: Call<Value> = RetrofitBuilder.builder(context).doAttendance(body, "123123")

        // Enqueue the call to run it asynchronously
        call.enqueue(object : Callback<Value> {
            override fun onResponse(call: Call<Value>, response: Response<Value>) {
                // Handle the response
                if (response.isSuccessful) {
                    val value = response.body()?.value
                    // Do something with the result
                } else {
                    // Handle error
                }
            }

            override fun onFailure(call: Call<Value>, t: Throwable) {
                // Handle failure
            }
        })
    }

}