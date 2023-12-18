package com.yudas1337.recognizeface.screens

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.cardview.widget.CardView
import com.yudas1337.recognizeface.R
import com.yudas1337.recognizeface.database.DBHelper
import com.yudas1337.recognizeface.database.Table
import com.yudas1337.recognizeface.helpers.MLVideoHelperActivity
import com.yudas1337.recognizeface.helpers.PackageHelper

class MenuActivity : AppCompatActivity() {

    private lateinit var presentCard: CardView
    private lateinit var presentListCard: CardView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu);

        val dbHelper = DBHelper(this, null)

        val timestamp = PackageHelper.timestamp

        presentCard = findViewById(R.id.present_card)
        presentListCard = findViewById(R.id.present_list_card)

        presentCard.setOnClickListener {
            val data = mapOf("id" to  PackageHelper.uuid, "role_name" to "admin", "created_at" to timestamp, "updated_at" to timestamp)
            val insert = dbHelper.insertData(Table.roles, data)
            Log.d("myTag", "BERHASIL INSERT BANGGG $insert")
            startActivity(Intent(this, ScanActivity::class.java))
            finish()
        }

        presentListCard.setOnClickListener{
            startActivity(Intent(this, MLVideoHelperActivity::class.java))
            finish()
        }
    }
}