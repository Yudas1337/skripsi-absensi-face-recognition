package com.yudas1337.recognizeface.screens

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.lifecycle.LifecycleObserver
import com.yudas1337.recognizeface.R
import com.yudas1337.recognizeface.database.DBHelper
import com.yudas1337.recognizeface.helpers.AlertHelper
import com.yudas1337.recognizeface.helpers.PackageHelper
import com.yudas1337.recognizeface.helpers.VoiceHelper
import com.yudas1337.recognizeface.network.NetworkConnection

class MenuActivity : AppCompatActivity(), LifecycleObserver {
    private lateinit var networkConnection: NetworkConnection
    private lateinit var presentCard: CardView
    private lateinit var userCard: CardView
    private lateinit var syncMenu: CardView
    private lateinit var presentDayCard: CardView
    private var isInternetAvailable: Boolean = false
    private var voiceHelper: VoiceHelper? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        syncMenu = findViewById(R.id.sync_menu)
        presentCard = findViewById(R.id.present_card)
        userCard = findViewById(R.id.user_card)
        presentDayCard = findViewById(R.id.present_day)

        voiceHelper = VoiceHelper.getInstance(this)
        voiceHelper!!.initializeTextToSpeech(this)

        networkConnection = NetworkConnection(applicationContext)
        lifecycle.addObserver(this)
        networkConnection.observe(this){ isInternetAvailable = it }

        syncMenu.setOnClickListener {
            if(isInternetAvailable){
                AlertHelper.internetAvailable(this,
                    { Toast.makeText(this, "OK CONFIRM", Toast.LENGTH_SHORT).show() },
                    { Toast.makeText(this, "GAJADI", Toast.LENGTH_SHORT).show() }
                )
            } else{
                AlertHelper.internetNotAvailable(this)
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
            voiceHelper!!.runVoice("Presensi Berhasil")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        networkConnection.clearObserver()
        voiceHelper!!.stopAndShutdown()
    }
}