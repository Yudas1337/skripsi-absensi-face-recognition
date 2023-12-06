package com.yudas1337.recognizeface.screens

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.yudas1337.recognizeface.R

class ScanActivity : AppCompatActivity() {

    private lateinit var editText: EditText
    private lateinit var imageView: ImageView
    private lateinit var button: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan)

        editText = findViewById(R.id.editText)
        imageView = findViewById(R.id.imageView)
        button = findViewById(R.id.button)

        imageView.setImageResource(R.drawable.rfid_illustration)

        button.setOnClickListener {
            val RFID_CARD = editText.text.toString()
            if (RFID_CARD.isNotEmpty()) {
                Toast.makeText(this@ScanActivity, "Melakukan scan ID Card: $RFID_CARD", Toast.LENGTH_SHORT).show()
                val intent = Intent(this@ScanActivity, MainActivity::class.java)
                    .putExtra("RFID_CARD", RFID_CARD)
                startActivity(intent)
            } else {
                Toast.makeText(this@ScanActivity, "Harap isi ID Card Anda", Toast.LENGTH_SHORT).show()
            }
        }
    }
}