package com.yudas1337.recognizeface.screens

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LifecycleObserver
import com.yudas1337.recognizeface.R
import com.yudas1337.recognizeface.helpers.AlertHelper
import com.yudas1337.recognizeface.network.NetworkConnection

class SyncActivity : AppCompatActivity(), LifecycleObserver {

    private lateinit var networkConnection: NetworkConnection
    private var isInternetAvailable: Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sync)

        networkConnection = NetworkConnection(applicationContext)
        lifecycle.addObserver(this)
        networkConnection.observe(this){ isInternetAvailable = it }


        if(isInternetAvailable){
        } else{
            AlertHelper.internetNotAvailable(this)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        networkConnection.clearObserver()
    }
}
