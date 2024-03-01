package com.yudas1337.recognizeface.screens

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.lifecycle.LifecycleObserver
import com.yudas1337.recognizeface.R
import com.yudas1337.recognizeface.constants.ConstShared
import com.yudas1337.recognizeface.database.SharedPref
import com.yudas1337.recognizeface.helpers.AlertHelper
import com.yudas1337.recognizeface.network.NetworkConnection

class MenuActivity : AppCompatActivity(), LifecycleObserver {
    private lateinit var presentCard: CardView
    private lateinit var userCard: CardView
    private lateinit var syncMenu: CardView
    private lateinit var presentDayCard: CardView

    private lateinit var networkConnection: NetworkConnection

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        syncMenu = findViewById(R.id.sync_menu)
        presentCard = findViewById(R.id.present_card)
        userCard = findViewById(R.id.user_card)
        presentDayCard = findViewById(R.id.present_day)

        networkConnection = NetworkConnection(this)
        lifecycle.addObserver(this)

        sharedPreferences = getSharedPreferences(ConstShared.fileName, Context.MODE_PRIVATE)

        syncMenu.setOnClickListener {
            networkConnection.observe(this){
                if(it){
                    startActivity(Intent(this, SyncActivity::class.java))
                    finish()
                } else{
                    AlertHelper.internetNotAvailable(this)
                }
            }

        }

        presentCard.setOnClickListener {
            if(fetchAvailableData()){
                startActivity(Intent(this, ScanActivity::class.java))
                finish()
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

//        if(SharedPref.getInt(sharedPreferences, ConstShared.TOTAL_FACES) == 0){
//            AlertHelper.errorDialog(this, contentText = "Data wajah pegawai masih kosong. Silahkan sinkronisasi ulang!")
//            return false
//        }

        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        if(networkConnection.isInitialized){
            networkConnection.clearObserver()
        }
    }
}