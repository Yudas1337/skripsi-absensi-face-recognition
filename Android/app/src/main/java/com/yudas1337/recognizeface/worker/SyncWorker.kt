package com.yudas1337.recognizeface.worker

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.yudas1337.recognizeface.database.DBHelper
import com.yudas1337.recognizeface.services.ApiService

class SyncWorker(context: Context, workerParams: WorkerParameters
) : Worker(context, workerParams) {

    override fun doWork(): Result {
        return try {
            ApiService(applicationContext).syncAttendances()
            Log.d("wajahnya", "worker berhasil jalan")
            Result.success()
        } catch(e: Exception){
            Log.d("wajahnya", "gagal gan ${e.message}")
            Result.failure()
        }
    }
}