package com.yudas1337.recognizeface.recognize

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.net.Uri
import android.provider.DocumentsContract
import android.util.Log
import android.widget.Toast
import androidx.documentfile.provider.DocumentFile
import androidx.exifinterface.media.ExifInterface
import cn.pedant.SweetAlert.SweetAlertDialog
import com.yudas1337.recognizeface.constants.ConstShared
import com.yudas1337.recognizeface.database.SharedPref
import com.yudas1337.recognizeface.helpers.AlertHelper
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream

class LoadFace(private val context: Context, private val frameAnalyser: FrameAnalyser, private val sharedPreferences: SharedPreferences) {

    private lateinit var dialog: SweetAlertDialog
    private var faceSize: Int = 0

    companion object {

        const val REQUEST_CODE_CHOOSE_DIRECTORY = 1001

        private var isSerializedDataStored = false

    }

    private fun launchChooseDirectoryIntent(activity: Activity) {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT_TREE)

        activity.startActivityForResult(intent, REQUEST_CODE_CHOOSE_DIRECTORY)
    }

    fun launchDocumentTree(requestCode: Int, resultCode: Int, data: Intent?, fileReader: FileReader, activity: Activity){
        if (requestCode == REQUEST_CODE_CHOOSE_DIRECTORY && resultCode == Activity.RESULT_OK) {
            val dirUri = data?.data
            val childrenUri = DocumentsContract.buildChildDocumentsUriUsingTree(
                dirUri,
                DocumentsContract.getTreeDocumentId(dirUri)
            )
            val tree = DocumentFile.fromTreeUri(context, childrenUri)
            val images = ArrayList<Pair<String, Bitmap>>()
            var errorFound = false
            if (tree?.listFiles()?.isNotEmpty() == true) {
                for (doc in tree.listFiles() ?: emptyArray()) {
                    if (doc.isDirectory && !errorFound) {
                        val name = doc.name ?: continue
                        for (imageDocFile in doc.listFiles() ?: emptyArray()) {
                            try {
                                images.add(Pair(name, getFixedBitmap(imageDocFile.uri)))
                            } catch (e: Exception) {
                                errorFound = true
                                Toast.makeText(context, "Parsing Image Error. Pastikan struktur folder benar", Toast.LENGTH_SHORT).show()
                                break
                            }
                        }
                        Log.d("wajahnya","Found ${doc.listFiles().size} images in $name directory")
                    } else {
                        errorFound = true
                        AlertHelper.errorDialogWithButton(context, "Parsing Error",
                            "Tidak ditemukan list gambar wajah atau struktur folder salah", "RESELECT", "CANCEL",
                            { launchChooseDirectoryIntent(activity) },
                            {})
                    }
                }
            } else {
                errorFound = true
                AlertHelper.errorDialogWithButton(context, "Parsing Error",
                    "Tidak ditemukan list gambar wajah atau struktur folder salah", "RESELECT", "CANCEL",
                    { launchChooseDirectoryIntent(activity) },
                    {})
            }
            if (!errorFound) {
                dialog = AlertHelper.progressDialog(context, "Parsing Image to TFlite Model")
                dialog.show()
                fileReader.run(images, fileReaderCallback)
                faceSize = images.size
                Log.d("wajahnya", "Terdeteksi $faceSize wajah gambar ...")
            } else {
                AlertHelper.errorDialogWithButton(context, "Parsing Error",
                    "Tidak ditemukan list gambar wajah atau struktur folder salah", "RESELECT", "CANCEL",
                    { launchChooseDirectoryIntent(activity) },
                    {})
            }
        }
    }

    // Get the image as a Bitmap from given Uri and fix the rotation using the Exif interface
    // Source -> https://stackoverflow.com/questions/14066038/why-does-an-image-captured-using-camera-intent-gets-rotated-on-some-devices-on-a
    private fun getFixedBitmap( imageFileUri : Uri) : Bitmap {
        var imageBitmap = BitmapUtils.getBitmapFromUri(context.contentResolver , imageFileUri )
        val exifInterface = ExifInterface(context.contentResolver.openInputStream( imageFileUri )!! )
        imageBitmap =
            when (exifInterface.getAttributeInt( ExifInterface.TAG_ORIENTATION ,
                ExifInterface.ORIENTATION_UNDEFINED )) {
                ExifInterface.ORIENTATION_ROTATE_90 -> BitmapUtils.rotateBitmap( imageBitmap , 90f )
                ExifInterface.ORIENTATION_ROTATE_180 -> BitmapUtils.rotateBitmap( imageBitmap , 180f )
                ExifInterface.ORIENTATION_ROTATE_270 -> BitmapUtils.rotateBitmap( imageBitmap , 270f )
                else -> imageBitmap
            }
        return imageBitmap
    }

    private val fileReaderCallback = object : FileReader.ProcessCallback {
        override fun onProcessCompleted(data: ArrayList<Pair<String, FloatArray>>, numImagesWithNoFaces: Int) {

            frameAnalyser.faceList = data

            saveSerializedImageData(data)

            dialog.dismissWithAnimation()
            AlertHelper.successDialog(context, "Image Parsed",
                "Total $faceSize gambar, $numImagesWithNoFaces gambar tidak terdeteksi wajah ")
        }
    }


    private fun saveSerializedImageData(data : ArrayList<Pair<String,FloatArray>> ) {
        val serializedDataFile = File(context.filesDir , ConstShared.SERIALIZED_DATA_FILENAME)

        ObjectOutputStream( FileOutputStream( serializedDataFile )  ).apply {
            writeObject( data )
            flush()
            close()
        }

        SharedPref.putInt(sharedPreferences, ConstShared.TOTAL_FACES, frameAnalyser.faceList.size)
        SharedPref.putBoolean(sharedPreferences, ConstShared.SHARED_PREF_IS_DATA_STORED_KEY , true)
    }


     fun loadSerializedImageData() : ArrayList<Pair<String,FloatArray>> {
        val serializedDataFile = File(context.filesDir , ConstShared.SERIALIZED_DATA_FILENAME )
        val objectInputStream = ObjectInputStream(FileInputStream( serializedDataFile ) )
        val data = objectInputStream.readObject() as ArrayList<Pair<String,FloatArray>>
        objectInputStream.close()
        return data
    }

    // Open File chooser to choose the images directory.
    private fun showSelectDirectoryDialog(activity: Activity) {
        AlertHelper.selectDirectoryDialog(context,"Pilih Folder Berisi Wajah",
            "Silahkan pilih folder berisi gambar wajah pegawai", "PILIH"
        ) {
            launchChooseDirectoryIntent(activity)
        }
    }

    fun loadListFaces(activity: Activity){
        isSerializedDataStored = SharedPref.isSerializedDataStored(sharedPreferences, ConstShared.SHARED_PREF_IS_DATA_STORED_KEY)
        if ( !isSerializedDataStored ) {
            showSelectDirectoryDialog(activity)
        }
        else {

            AlertHelper.warningDialogWithButton(context, "Data Wajah ditemukan",
                "Data wajah sudah terdaftar. Apa anda ingin menggantinya?",
                "Ganti Data",
                "Gunakan Data Lama", {
                    launchChooseDirectoryIntent(activity)
                }, {
                    frameAnalyser.faceList = loadSerializedImageData()
                    AlertHelper.successDialog(context, contentText = "Data wajah berhasil disimpan")
                    Log.d("wajahnya", "Serialized data loaded.")
                    Log.d("wajahnya", "${frameAnalyser.faceList.size}")
                })
        }
    }

}