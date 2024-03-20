package com.yudas1337.recognizeface.worker

import android.content.Context
import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkInfo
import androidx.work.WorkManager
import java.util.Calendar
import java.util.concurrent.TimeUnit

class RunWorker(private val context: Context, private val lifecycleOwner: LifecycleOwner) {

    fun scheduleWork() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresCharging(false)
            .build()

        val currentTime = Calendar.getInstance()
        val fivePm = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 17)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
        }

        val initialDelay = if (currentTime.after(fivePm)) {
            // Jika waktu saat ini sudah melewati jam 5 sore, maka atur jadwal tugas untuk besok
            val tomorrowFivePm = fivePm.clone() as Calendar
            tomorrowFivePm.add(Calendar.DAY_OF_MONTH, 1)
            tomorrowFivePm.timeInMillis - currentTime.timeInMillis
        } else {
            // Jika waktu saat ini masih sebelum jam 5 sore, atur jadwal tugas untuk hari ini
            fivePm.timeInMillis - currentTime.timeInMillis
        }

        val periodicWorkRequest = PeriodicWorkRequest.Builder(
            SyncWorker::class.java,
            15,
            TimeUnit.MINUTES)
            .setConstraints(constraints)
            .setInitialDelay(initialDelay, TimeUnit.MILLISECONDS)
            .build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            "syncAttendanceWork",
            ExistingPeriodicWorkPolicy.KEEP,
            periodicWorkRequest)

        Log.d("wajahnya", "berhasil inisialisasi")

        val workInfoLiveData = WorkManager.getInstance(context).getWorkInfoByIdLiveData(periodicWorkRequest.id)
        workInfoLiveData.observe(lifecycleOwner) { workInfo ->
            if (workInfo != null) {
                when (workInfo.state) {
                    WorkInfo.State.SUCCEEDED -> {
                        Log.d("wajahnya", "berhasil dijalankan")
                    }
                    WorkInfo.State.FAILED -> {
                        Log.d("wajahnya", "gagal dijalankan")
                    }
                    WorkInfo.State.CANCELLED -> {
                        Log.d("wajahnya", "dibatalkan")
                    }
                    WorkInfo.State.ENQUEUED -> {
                        Log.d("wajahnya", "antri")
                    }
                    WorkInfo.State.RUNNING -> {
                        Log.d("wajahnya", "lagi jalan")
                    }
                    WorkInfo.State.BLOCKED -> {
                        Log.d("wajahnya", "di blokir")
                    }
                }
            }
        }
    }

}