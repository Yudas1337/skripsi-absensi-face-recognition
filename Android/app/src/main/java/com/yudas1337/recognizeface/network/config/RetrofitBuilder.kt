package com.yudas1337.recognizeface.network.config

import com.yudas1337.recognizeface.constants.URL
import com.yudas1337.recognizeface.network.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitBuilder {

    fun builder(): Request {
        val retrofit = Retrofit.Builder()
            .baseUrl(URL.IP)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create(Request::class.java)
    }

    fun employeeBuilder(): Request{
        val retrofit = Retrofit.Builder()
            .baseUrl(URL.IP_PEGAWAI)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create(Request::class.java)
    }
}