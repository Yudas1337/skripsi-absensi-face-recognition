package com.yudas1337.recognizeface.intro

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.yudas1337.recognizeface.R
import com.yudas1337.recognizeface.screens.MenuActivity

class SplashActivity : AppCompatActivity() {
    private val splashTimeout = 2000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler().postDelayed({
            startActivity(Intent(this, MenuActivity::class.java))
            finish()
        }, splashTimeout.toLong())
    }
}