package com.yudas1337.recognizeface.screens

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.lifecycle.LifecycleObserver
import cn.pedant.SweetAlert.SweetAlertDialog
import com.yudas1337.recognizeface.R
import com.yudas1337.recognizeface.constants.ConstShared
import com.yudas1337.recognizeface.constants.ModelControl
import com.yudas1337.recognizeface.database.DBHelper
import com.yudas1337.recognizeface.helpers.AlertHelper
import com.yudas1337.recognizeface.network.NetworkConnection
import com.yudas1337.recognizeface.recognize.FileReader
import com.yudas1337.recognizeface.recognize.FrameAnalyser
import com.yudas1337.recognizeface.recognize.LoadFace
import com.yudas1337.recognizeface.recognize.model.FaceNetModel
import com.yudas1337.recognizeface.services.SyncService
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SyncActivity : AppCompatActivity(), LifecycleObserver {

    private lateinit var btnBack: Button
    private lateinit var firstMenu: LinearLayout
    private lateinit var secondMenu: LinearLayout
    private lateinit var thirdMenu: LinearLayout
    private lateinit var fourthMenu: LinearLayout

    private lateinit var networkConnection: NetworkConnection
    private var isInternetAvailable: Boolean = false

    lateinit var pDialog: SweetAlertDialog

    private lateinit var frameAnalyser  : FrameAnalyser
    private lateinit var faceNetModel : FaceNetModel
    private lateinit var fileReader : FileReader
    private lateinit var loadFace: LoadFace

    private var isInitialized : Boolean = false

    private lateinit var sharedPreferences: SharedPreferences

    private val defaultScope = CoroutineScope( Dispatchers.Default )

    val deferred = CompletableDeferred<Unit>()

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
                AlertHelper.doSync(this) {
                    pDialog = AlertHelper.progressDialog(this, percentageProgress)
                    pDialog.show()
                    SyncService(this, dbHelper).syncUsers()
                }
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
                SyncService(this, dbHelper).syncAttendances()
                Toast.makeText(this, "Menu 3", Toast.LENGTH_SHORT).show()
            } else{
                AlertHelper.internetNotAvailable(this)
            }
        }

        fourthMenu.setOnClickListener {

            if(!isInitialized){
                pDialog = AlertHelper.progressDialog(this, "Load Model..")
                pDialog.show()

                GlobalScope.launch(Dispatchers.Default) {
                    initializeModels()
                    deferred.complete(Unit)
                    withContext(Dispatchers.Main) {
                        pDialog.dismissWithAnimation()
                        isInitialized = true
                        loadFaceDirectory()
                    }
                }
            } else{
                loadFaceDirectory()
            }
        }

        btnBack.setOnClickListener{
            backService()
        }
    }

    private suspend fun initializeModels(): Unit = defaultScope.async {
        faceNetModel = FaceNetModel(this@SyncActivity, ModelControl.modelInfo, ModelControl.useGpu, ModelControl.useXNNPack)
        frameAnalyser = FrameAnalyser(this@SyncActivity, faceNetModel)
        fileReader = FileReader(faceNetModel)

        Unit
    }.await()

    private fun loadFaceDirectory(){
        sharedPreferences = getSharedPreferences(ConstShared.fileName, MODE_PRIVATE)

        loadFace = LoadFace(this, frameAnalyser, sharedPreferences)
        loadFace.loadListFaces(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        loadFace.launchDocumentTree(requestCode, resultCode, data, fileReader, this)
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
