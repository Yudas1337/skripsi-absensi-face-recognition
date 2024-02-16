package com.yudas1337.recognizeface.screens

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.lifecycle.LifecycleObserver
import com.yudas1337.recognizeface.R
import com.yudas1337.recognizeface.helpers.AlertHelper
import com.yudas1337.recognizeface.network.NetworkConnection

class MenuActivity : AppCompatActivity(), LifecycleObserver {
    private lateinit var presentCard: CardView
    private lateinit var userCard: CardView
    private lateinit var syncMenu: CardView
    private lateinit var presentDayCard: CardView

    private lateinit var networkConnection: NetworkConnection

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        syncMenu = findViewById(R.id.sync_menu)
        presentCard = findViewById(R.id.present_card)
        userCard = findViewById(R.id.user_card)
        presentDayCard = findViewById(R.id.present_day)

        networkConnection = NetworkConnection(this)
        lifecycle.addObserver(this)

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
            startActivity(Intent(this, ScanActivity::class.java))
            finish()
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

    override fun onDestroy() {
        super.onDestroy()
        if(networkConnection.isInitialized){
            networkConnection.clearObserver()
        }
    }
}