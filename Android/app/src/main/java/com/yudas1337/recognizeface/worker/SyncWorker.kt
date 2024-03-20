package com.yudas1337.recognizeface.worker

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters

class SyncWorker(context: Context, workerParams: WorkerParameters
) : Worker(context, workerParams) {

    override fun doWork(): Result {
        return try {
            Log.d("wajahnya", "worker berhasil jalan bosskuu")
            Result.success()
        } catch(e: Exception){
            Log.d("wajahnya", "gagal gan ${e.message}")
            Result.failure()
        }
    }
}