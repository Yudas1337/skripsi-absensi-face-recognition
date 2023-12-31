package com.yudas1337.recognizeface.screens.intro

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleObserver
import com.yudas1337.recognizeface.R
import com.yudas1337.recognizeface.helpers.AlertHelper
import com.yudas1337.recognizeface.network.NetworkConnection
import com.yudas1337.recognizeface.screens.MenuActivity

class SplashActivity : AppCompatActivity(), LifecycleObserver {
    private var splashTimeout = 2000
    private lateinit var networkConnection: NetworkConnection
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        networkConnection = NetworkConnection(applicationContext)
        lifecycle.addObserver(this)
        networkConnection.observe(this){
            if(it){
                splashTimeout = 500
                AlertHelper.internetAvailable(this,
                    {
                        Toast.makeText(this, "OK CONFIRM", Toast.LENGTH_SHORT).show()
                        nextActivity()
                    },
                    { nextActivity() }
                )
            } else{
                nextActivity()
            }
        }

    }

    private fun nextActivity(){
        Handler().postDelayed({
            startActivity(Intent(this, MenuActivity::class.java))
            finish()
        }, splashTimeout.toLong())
    }

    override fun onDestroy() {
        super.onDestroy()
        networkConnection.clearObserver()
    }
}