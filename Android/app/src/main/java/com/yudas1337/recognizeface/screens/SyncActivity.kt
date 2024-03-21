package com.yudas1337.recognizeface.screens

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.LifecycleObserver
import cn.pedant.SweetAlert.SweetAlertDialog
import com.yudas1337.recognizeface.R
import com.yudas1337.recognizeface.database.DBHelper
import com.yudas1337.recognizeface.helpers.AlertHelper
import com.yudas1337.recognizeface.network.NetworkConnection
import com.yudas1337.recognizeface.services.SyncService

class SyncActivity : AppCompatActivity(), LifecycleObserver {

    private lateinit var btnBack: Button
    private lateinit var firstMenu: LinearLayout
    private lateinit var secondMenu: LinearLayout
    private lateinit var thirdMenu: LinearLayout
    private lateinit var fourthMenu: LinearLayout

    private lateinit var networkConnection: NetworkConnection
    private var isInternetAvailable: Boolean = false

    lateinit var pDialog: SweetAlertDialog

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sync)

        btnBack = findViewById(R.id.btn_bck)
        firstMenu = findViewById(R.id.menu_1)
        secondMenu = findViewById(R.id.menu_2)
        thirdMenu = findViewById(R.id.menu_3)
        fourthMenu = findViewById(R.id.menu_4)

        networkConnection = NetworkConnection(this)
        lifecycle.addObserver(this)
        networkConnection.observe(this){ isInternetAvailable = it }

        pDialog = AlertHelper.progressDialog(this, percentageProgress)

        val dbHelper = DBHelper(this, null)

        firstMenu.setOnClickListener {
            if(isInternetAvailable){
                AlertHelper.doSyncWithPictures(this,
                    {
                        pDialog = AlertHelper.progressDialog(this, percentageProgress)
                        pDialog.show()
                        SyncService(this, dbHelper).syncEmployees()
                    },
                    {
                        pDialog = AlertHelper.progressDialog(this, percentageProgress)
                        pDialog.show()
                        SyncService(this, dbHelper).syncStudents()
                    }, "Pegawai", "Siswa Magang")
            } else{
                AlertHelper.internetNotAvailable(this)
            }
        }

        secondMenu.setOnClickListener {
            if(isInternetAvailable){
                AlertHelper.doSync(this){
                    pDialog = AlertHelper.progressDialog(this, percentageProgress)
                    pDialog.show()
                    SyncService(this, dbHelper).syncSchedules()
                }

            } else{
                AlertHelper.internetNotAvailable(this)
            }
        }

        thirdMenu.setOnClickListener {
            if(isInternetAvailable){
                if(dbHelper.countUnsyncAttendances() != 0){
                    pDialog = AlertHelper.progressDialog(this, percentageProgress)
                    pDialog.show()
                    SyncService(this, dbHelper).syncAttendances()
                    pDialog.dismissWithAnimation()
                } else{
                    AlertHelper.successDialog(this, "Data presensi lengkap!", "Seluruh data presensi telah tersinkronisasi")
                }
            } else{
                AlertHelper.internetNotAvailable(this)
            }
        }

        fourthMenu.setOnClickListener {
                if(isInternetAvailable){
                    AlertHelper.doSyncWithPictures(this,
                        {
                            pDialog = AlertHelper.progressDialog(this, percentageProgress)
                            pDialog.show()
                            SyncService(this, dbHelper).syncEmployeeFaces()
                        },
                        {
                            pDialog = AlertHelper.progressDialog(this, percentageProgress)
                            pDialog.show()
                            SyncService(this, dbHelper).syncStudentFaces()
                        }, "Wajah Pegawai", "Wajah Siswa Magang")
                } else{
                    AlertHelper.internetNotAvailable(this)
                }
        }

        btnBack.setOnClickListener{
            backService()
        }
    }

    companion object{
        var percentageProgress = "Loading.."
    }

    private fun backService(){
        startActivity(Intent(this@SyncActivity, MenuActivity::class.java))
        finish()
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        backService()
    }

    override fun onDestroy() {
        super.onDestroy()
        if(networkConnection.isInitialized){
            networkConnection.clearObserver()
        }
    }
}
