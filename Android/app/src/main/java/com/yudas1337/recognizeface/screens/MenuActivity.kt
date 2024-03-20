package com.yudas1337.recognizeface.screens

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.lifecycle.LifecycleObserver
import com.yudas1337.recognizeface.R
import com.yudas1337.recognizeface.constants.ConstShared
import com.yudas1337.recognizeface.constants.FaceFolder
import com.yudas1337.recognizeface.database.SharedPref
import com.yudas1337.recognizeface.helpers.AlertHelper
import com.yudas1337.recognizeface.helpers.PermissionHelper
import com.yudas1337.recognizeface.network.NetworkConnection
import com.yudas1337.recognizeface.services.CheckSumService
import com.yudas1337.recognizeface.worker.RunWorker


class MenuActivity : AppCompatActivity(), LifecycleObserver {
    private lateinit var presentCard: CardView
    private lateinit var userCard: CardView
    private lateinit var syncMenu: CardView
    private lateinit var presentDayCard: CardView
    private lateinit var networkConnection: NetworkConnection
    private var isInternetAvailable: Boolean = false
    private lateinit var sumService: CheckSumService
    private lateinit var sharedPreferences: SharedPreferences
    private var syncList: ArrayList<String> = ArrayList()
    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        syncMenu = findViewById(R.id.sync_menu)
        presentCard = findViewById(R.id.present_card)
        userCard = findViewById(R.id.user_card)
        presentDayCard = findViewById(R.id.present_day)

        networkConnection = NetworkConnection(this)
        lifecycle.addObserver(this)
        networkConnection.observe(this){ isInternetAvailable = it }
        sharedPreferences = getSharedPreferences(ConstShared.fileName, Context.MODE_PRIVATE)

        sumService = CheckSumService(sharedPreferences)

        networkConnection.observe(this){
            if(it){
                sumService.checkAllSum()
                syncAvailableData()
                RunWorker(this, this).scheduleWork()
            }
        }

        syncMenu.setOnClickListener {
            if (Environment.isExternalStorageManager()) {
                if(isInternetAvailable){
                    startActivity(Intent(this, SyncActivity::class.java))
                    finish()
                } else{
                    AlertHelper.internetNotAvailable(this)
                }
            } else {
                PermissionHelper.requestAccessFiles(this)
            }

        }

        presentCard.setOnClickListener {
            if (Environment.isExternalStorageManager() && fetchAvailableData()) {
                startActivity(Intent(this, ScanActivity::class.java))
                finish()
            } else {
                PermissionHelper.requestAccessFiles(this)
            }
        }

        userCard.setOnClickListener{
            startActivity(Intent(this, UserListActivity::class.java))
            finish()
        }

        presentDayCard.setOnClickListener {
            startActivity(Intent(this, AttendanceTodayActivity::class.java))
            finish()
        }
    }

    private fun syncAvailableData(){
        syncList.clear()
        val dataToCheck = arrayOf(
            arrayOf(SharedPref.getString(sharedPreferences, ConstShared.FETCH_MD5_STUDENTS), SharedPref.getString(sharedPreferences, ConstShared.MD5_STUDENTS), "Data Siswa"),
            arrayOf(SharedPref.getString(sharedPreferences, ConstShared.FETCH_MD5_EMPLOYEES), SharedPref.getString(sharedPreferences, ConstShared.MD5_EMPLOYEES), "Data Pegawai"),
            arrayOf(SharedPref.getString(sharedPreferences, ConstShared.FETCH_MD5_SCHEDULES), SharedPref.getString(sharedPreferences, ConstShared.MD5_SCHEDULES), "Data Jadwal"),
            arrayOf(SharedPref.getString(sharedPreferences, ConstShared.FETCH_MD5_STUDENT_FACES ), SharedPref.getString(sharedPreferences, ConstShared.MD5_STUDENT_FACES), "Data Wajah Siswa"),
            arrayOf(SharedPref.getString(sharedPreferences, ConstShared.FETCH_MD5_EMPLOYEE_FACES), SharedPref.getString(sharedPreferences, ConstShared.MD5_EMPLOYEE_FACES), "Data Wajah Pegawai")
        )
        for (data in dataToCheck) {
            if (data[0] != data[1]) {
                Log.d("wajahnya", "fetchnya ${data[0]} di localnya ${data[1]}")
                syncList.add(data[2])
            }
        }

        if (syncList.isNotEmpty()) {
            val syncDataText = StringBuilder()
            syncList.forEachIndexed { index, item ->
                syncDataText.append("${index + 1}. $item").append(System.lineSeparator())
            }
            AlertHelper.warningDialog(this, "Peringatan Pembaruan Data", "Terdapat beberapa data yang perlu di sinkronisasi ulang: ${syncDataText}")
        }

    }

    private fun fetchAvailableData(): Boolean {
        if(SharedPref.getInt(sharedPreferences, ConstShared.TOTAL_STUDENTS)  == 0){
            AlertHelper.errorDialog(this, contentText = "Data siswa masih kosong. Silahkan sinkronisasi ulang!")
            return false
        }

        if(SharedPref.getInt(sharedPreferences, ConstShared.TOTAL_EMPLOYEES) == 0){
            AlertHelper.errorDialog(this, contentText = "Data pegawai masih kosong. Silahkan sinkronisasi ulang!")
            return false
        }

        if(SharedPref.getInt(sharedPreferences, ConstShared.TOTAL_SCHEDULES) == 0 && SharedPref.getInt(sharedPreferences, ConstShared.TOTAL_LIMIT) == 0){
            AlertHelper.errorDialog(this, contentText = "Data jadwal masih kosong. Silahkan sinkronisasi ulang!")
            return false
        }

        if(FaceFolder.facesDir.listFiles()?.isEmpty() == true){
            AlertHelper.errorDialog(this, contentText = "Data wajah pengguna masih kosong. Silahkan sinkronisasi ulang!")
            return false
        }

        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        if(networkConnection.isInitialized){
            networkConnection.clearObserver()
        }
    }
}