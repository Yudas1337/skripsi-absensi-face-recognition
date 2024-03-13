package com.yudas1337.recognizeface.recognize

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import android.util.Log
import android.widget.Toast
import cn.pedant.SweetAlert.SweetAlertDialog
import com.yudas1337.recognizeface.constants.ConstShared
import com.yudas1337.recognizeface.constants.FaceFolder
import com.yudas1337.recognizeface.constants.Role
import com.yudas1337.recognizeface.database.SharedPref
import com.yudas1337.recognizeface.helpers.AlertHelper
import com.yudas1337.recognizeface.helpers.VoiceHelper
import com.yudas1337.recognizeface.screens.MainActivity
import com.yudas1337.recognizeface.screens.ScanActivity
import kotlinx.coroutines.ObsoleteCoroutinesApi
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream


class LoadFace(private val context: Context, private val frameAnalyser: FrameAnalyser, private val sharedPreferences: SharedPreferences) {

    private lateinit var dialog: SweetAlertDialog
    private var faceSize: Int = 0

    private lateinit var scanData: HashMap<String, String?>
    private var rfidUser: String? = null

    companion object {

        const val REQUEST_CODE_CHOOSE_DIRECTORY = 1001
        private var isSerializedDataStored = false

    }

    private fun launchChooseDirectoryIntent(activity: Activity) {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT_TREE)

        activity.startActivityForResult(intent, REQUEST_CODE_CHOOSE_DIRECTORY)
    }

    @OptIn(ObsoleteCoroutinesApi::class)
    private val fileReaderCallback = object : FileReader.ProcessCallback {
        override fun onProcessCompleted(data: ArrayList<Pair<String, FloatArray>>, numImagesWithNoFaces: Int) {

            frameAnalyser.faceList = data
            saveSerializedImageData(data, rfidUser)

            dialog.dismissWithAnimation()

            val run = context as Activity
            run.runOnUiThread{
                context.startActivityForResult(Intent(context, MainActivity::class.java), ScanActivity.REQUEST_CODE_MAIN)
            }
        }
    }


    private fun saveSerializedImageData(data : ArrayList<Pair<String,FloatArray>>, rfid: String?) {
        val serializedDataFile = File(context.filesDir , "${rfid}-${ConstShared.SERIALIZED_DATA_FILENAME}")

        ObjectOutputStream( FileOutputStream( serializedDataFile )  ).apply {
            writeObject( data )
            flush()
            close()
        }

        SharedPref.putInt(sharedPreferences, "${rfid}-${ConstShared.TOTAL_EXTRACTED_FACES}", frameAnalyser.faceList.size)
        SharedPref.putBoolean(sharedPreferences, "${rfid}-${ConstShared.SHARED_PREF_IS_DATA_STORED_KEY}", true)
    }

     fun loadSerializedImageData(rfid: String) : ArrayList<Pair<String,FloatArray>> {
        val serializedDataFile = File(context.filesDir , "${rfid}-${ConstShared.SERIALIZED_DATA_FILENAME}")
        val objectInputStream = ObjectInputStream(FileInputStream( serializedDataFile ) )
        val data = objectInputStream.readObject() as ArrayList<Pair<String,FloatArray>>
        objectInputStream.close()
        return data
    }

    @OptIn(ObsoleteCoroutinesApi::class)
    fun readFileUsingRfid(rfid: String, fileReader: FileReader, data: HashMap<String, String?>, voiceHelper: VoiceHelper): Boolean {
        rfidUser = rfid

        if (SharedPref.isSerializedDataStored(sharedPreferences, "${rfid}-${ConstShared.SHARED_PREF_IS_DATA_STORED_KEY}")) {
            val run = context as Activity
            run.runOnUiThread{
                context.startActivityForResult(Intent(context, MainActivity::class.java), ScanActivity.REQUEST_CODE_MAIN)
            }
            return true
        }

        scanData = data
        val images = ArrayList<Pair<String, Bitmap>>()

        val facesDir = FaceFolder.facesDir

        if (facesDir.isDirectory && facesDir.exists()) {
            val role = if(Role.EMPLOYEE == scanData["role"].toString())
            { FaceFolder.EMPLOYEE_DIR_FACES_NAME } else{ FaceFolder.STUDENTS_DIR_FACES_NAME}

            val rfidDir = File(facesDir, "$role/$rfid")

            if (rfidDir.isDirectory && rfidDir.exists()) {
                val name = rfidDir.name
                for (imageDocFile in rfidDir.listFiles() ?: emptyArray()) {
                    try {
                        images.add(Pair(name, BitmapUtils.getFixedBitmap(Uri.fromFile(imageDocFile), context)))
                    } catch (e: Exception) {
                        Log.e("wajahnya", "Parsing Image Error. Pastikan struktur folder benar")
                        return false
                    }
                }
                Log.d("wajahnya", "jumlah file nya ${rfidDir.listFiles()?.size}")
            } else {
                AlertHelper.runVoiceAndToast(voiceHelper, context, "Wajah pengguna tidak terdaftar. Harap Sinkronisasi Ulang")
                return false
            }
        } else{
            Toast.makeText(context, "Folder ${FaceFolder.DIR_FACES_NAME } tidak ditemukan. Harap Sinkronisasi Ulang", Toast.LENGTH_SHORT).show()
            return false
        }

        dialog = AlertHelper.progressDialog(context, "Extracting Saved Face..")
        dialog.show()

        fileReader.run(images, fileReaderCallback)

        faceSize = images.size
        Log.d("wajahnya", "Terdeteksi $faceSize wajah gambar ...")

        return true
    }
}