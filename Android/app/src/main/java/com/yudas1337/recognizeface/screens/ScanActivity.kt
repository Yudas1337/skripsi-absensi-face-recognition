package com.yudas1337.recognizeface.screens

import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import cn.pedant.SweetAlert.SweetAlertDialog
import com.yudas1337.recognizeface.R
import com.yudas1337.recognizeface.constants.ConstShared
import com.yudas1337.recognizeface.database.DBHelper
import com.yudas1337.recognizeface.helpers.AlertHelper
import com.yudas1337.recognizeface.helpers.VoiceHelper
import com.yudas1337.recognizeface.recognize.CustomDispatcher
import com.yudas1337.recognizeface.recognize.FaceUtil
import com.yudas1337.recognizeface.recognize.FileReader
import com.yudas1337.recognizeface.recognize.FrameAnalyser
import com.yudas1337.recognizeface.recognize.LoadFace
import com.yudas1337.recognizeface.recognize.model.FaceNetModel
import com.yudas1337.recognizeface.services.ScanService
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import com.yudas1337.recognizeface.constants.FaceStatus
import com.yudas1337.recognizeface.helpers.BrightnessHelper
import com.yudas1337.recognizeface.recognize.BitmapUtils
import kotlinx.coroutines.DelicateCoroutinesApi

class ScanActivity : AppCompatActivity() {

    private lateinit var relativeOne: RelativeLayout
    private lateinit var rfidController: EditText
    private lateinit var copyRight: TextView
    private lateinit var btnSubmit: Button
    private var voiceHelper: VoiceHelper? = null
    private lateinit var dbHelper: DBHelper

    private lateinit var frameAnalyser: FrameAnalyser
    private lateinit var faceNetModel: FaceNetModel
    private lateinit var loadFace: LoadFace
    private lateinit var fileReader: FileReader
    private var userFace: Boolean = false

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var pDialog: SweetAlertDialog

    private var isInitialized: Boolean = false
    private val deferred = CompletableDeferred<Unit>()

    private lateinit var scanData: HashMap<String, String?>

    private lateinit var scanService: ScanService

    private var settingsCanWrite: Boolean = false


    companion object {
        const val REQUEST_CODE_MAIN = 1
        var croppedBitmap: Bitmap? = null
    }

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan)

        relativeOne = findViewById(R.id.relative_1)
        copyRight = findViewById(R.id.cr_2)
        rfidController = findViewById(R.id.rfidController)
        btnSubmit = findViewById(R.id.btn_submit)

        voiceHelper = VoiceHelper.getInstance(this)
        voiceHelper!!.initializeTextToSpeech(this)

        relativeOne.setOnClickListener {
            backService()
        }

        dbHelper = DBHelper(this, null)
        sharedPreferences = getSharedPreferences(ConstShared.fileName, MODE_PRIVATE)

        val currentYear = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy"))
        val copyrightText = "$currentYear Hummatech All Rights Reserved."
        copyRight.text = copyrightText

        if (!isInitialized) {
            pDialog = AlertHelper.progressDialog(this, "Load Model..")
            pDialog.show()
            GlobalScope.launch(Dispatchers.Default) {
                initializeModels()
                deferred.complete(Unit)
                withContext(Dispatchers.Main) {
                    pDialog.dismissWithAnimation()
                    isInitialized = true
                    Toast.makeText(this@ScanActivity, "Berhasil load Model", Toast.LENGTH_SHORT)
                        .show()
                    init()
                }
            }
        } else {
            init()
        }

    }

    private suspend fun initializeModels(): Unit = withContext(CustomDispatcher.dispatcher) {
        faceNetModel = FaceUtil.initializeFaceNetModel(this@ScanActivity)
        frameAnalyser = FaceUtil.initializeFrameAnalyser(this@ScanActivity, faceNetModel)

        loadFace = LoadFace(this@ScanActivity, frameAnalyser, sharedPreferences)
        fileReader = FileReader(faceNetModel)
    }

    @RequiresApi(Build.VERSION_CODES.R)
    private fun executeMain() {
        settingsCanWrite = BrightnessHelper.hasWriteSettingsPermission(this)

        BrightnessHelper.checkBrightnessPermission(this, 20)

        scanService = ScanService(this, dbHelper, voiceHelper!!)
        btnSubmit.setOnClickListener {
            val getRfid = rfidController.text
            val input = getRfid.toString()

            if (input.isNotEmpty()) {
                scanData = scanService.handleScan(input)
                getRfid.clear()
                if (scanData.isNotEmpty()) {
                    if(scanService.checkTodayAttendance(scanData["id"].toString())){
                        loadFace.readFileUsingRfid(input, fileReader, scanData, voiceHelper!!)
                    }
                } else {
                    AlertHelper.runVoiceAndToast(voiceHelper!!, this, "Kartu tidak terdaftar")
                }
            } else {
                AlertHelper.runVoiceAndToast(voiceHelper!!, this, "Harap Scan Kartu Anda")
            }
        }
    }


    @OptIn(DelicateCoroutinesApi::class)
    @RequiresApi(Build.VERSION_CODES.R)
    private fun init() {
        GlobalScope.launch(Dispatchers.Main) {
            deferred.await()
            executeMain()
        }
    }

    private fun backService() {
        startActivity(Intent(this@ScanActivity, MenuActivity::class.java))
        finish()
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        backService()
    }

    override fun onDestroy() {
        super.onDestroy()
        voiceHelper!!.stopAndShutdown()
    }

    @OptIn(DelicateCoroutinesApi::class)
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_MAIN && resultCode == RESULT_OK) {

            if(croppedBitmap != null){
                val pDialog = AlertHelper.progressDialog(this, "Recognizing..")
                pDialog.show()

//                val darkImage = BitmapUtils.isDark(croppedBitmap!!)
//
//                if(darkImage){
//                    Log.d("wajahnya", "gelap bang fotonya")
//                    croppedBitmap = BitmapUtils.contrastStretching(
//                        croppedBitmap!!, 0 ,255)
//                } else{
//                    Log.d("wajahnya", "tidak gelap fotonya")
//                }

                GlobalScope.launch(Dispatchers.Default) {

                    frameAnalyser.faceList = loadFace.loadSerializedImageData(scanData["rfid"].toString())

                    deferred.complete(Unit)
                    withContext(CustomDispatcher.dispatcher) {

                        // returnya adalah rfid pengguna, hanya untuk patokan berhasil
                        val analyser = frameAnalyser.runModel(croppedBitmap!!)

                        withContext(Dispatchers.Main){
                            pDialog.dismissWithAnimation()
                            when {
                                analyser.equals(FaceStatus.MASKED, true) -> AlertHelper.runVoiceAndToast(voiceHelper!!, this@ScanActivity, "Masker Terdeteksi atau Cahaya Terlalu Gelap")
                                analyser.equals(FaceStatus.UNKNOWN, true) -> AlertHelper.runVoiceAndToast(voiceHelper!!, this@ScanActivity, "Wajah tidak cocok dengan kartu")
                                else -> scanService.handleAttendances(scanData["id"].toString(), scanData["role"].toString(), scanData["name"].toString())
                            }
                        }
                    }
                }
            }

//            val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
//            val imageFile = File(downloadsDir, FaceFolder.CROPPED_FACE)
//
//            if (imageFile.exists()) {
//                val bitmap = BitmapFactory.decodeFile(imageFile.absolutePath)
//
//            }

            BrightnessHelper.checkBrightnessPermission(this, 20)
        }
    }

}