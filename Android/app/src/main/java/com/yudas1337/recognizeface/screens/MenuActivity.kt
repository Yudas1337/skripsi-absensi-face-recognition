package com.yudas1337.recognizeface.screens

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.cardview.widget.CardView
import com.yudas1337.recognizeface.R

class MenuActivity : AppCompatActivity() {

    private lateinit var presentCard: CardView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu);

        presentCard = findViewById(R.id.present_card)

        presentCard.setOnClickListener {
            startActivity(Intent(this, ScanActivity::class.java))
            finish()
        }
    }
}