package com.yudas1337.recognizeface.screens

import VoiceHelper
import android.content.Intent
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.lifecycle.LifecycleObserver
import com.yudas1337.recognizeface.R
import com.yudas1337.recognizeface.database.DBHelper
import com.yudas1337.recognizeface.database.Table
import com.yudas1337.recognizeface.helpers.AlertHelper
import com.yudas1337.recognizeface.helpers.PackageHelper
import com.yudas1337.recognizeface.network.NetworkConnection

class MenuActivity : AppCompatActivity(), LifecycleObserver {
    private lateinit var networkConnection: NetworkConnection
    private lateinit var presentCard: CardView
    private lateinit var presentListCard: CardView
    private lateinit var syncMenu: CardView
    private lateinit var presentDayCard: CardView
    private var isInternetAvailable: Boolean = false
    private var voiceHelper: VoiceHelper? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        val dbHelper = DBHelper(this, null)
        val timestamp = PackageHelper.timestamp
        syncMenu = findViewById(R.id.sync_menu)
        presentCard = findViewById(R.id.present_card)
        presentListCard = findViewById(R.id.present_list_card)
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
            val data = mapOf("id" to  PackageHelper.uuid, "role_name" to "admin", "created_at" to timestamp, "updated_at" to timestamp)
            val insert = dbHelper.insertData(Table.roles, data)
            Log.d("myTag", "BERHASIL INSERT BANGGG $insert")
            startActivity(Intent(this, ScanActivity::class.java))
            finish()
        }

        presentListCard.setOnClickListener{
            startActivity(Intent(this, ClassifyActivity::class.java))
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