package com.yudas1337.recognizeface.recognize

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.exifinterface.media.ExifInterface
import com.yudas1337.recognizeface.R
import com.yudas1337.recognizeface.helpers.AlertHelper
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream

class LoadFace(private val context: Context, private val frameAnalyser: FrameAnalyser) {

    companion object {

        private lateinit var sharedPreferences: SharedPreferences

        private var isSerializedDataStored = false

        // Serialized data will be stored ( in app's private storage ) with this filename.
        private val SERIALIZED_DATA_FILENAME = "image_data"

        // Shared Pref key to check if the data was stored.
        private val SHARED_PREF_IS_DATA_STORED_KEY = "is_data_stored"

        fun serializedCondition(): Boolean {
            return this.isSerializedDataStored
        }
    }

    private fun launchChooseDirectoryIntent() {
        val intent = Intent( Intent.ACTION_OPEN_DOCUMENT_TREE )
        // startForActivityResult is deprecated.
        // See this SO thread -> https://stackoverflow.com/questions/62671106/onactivityresult-method-is-deprecated-what-is-the-alternative
//        directoryAccessLauncher.launch( intent )
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
            saveSerializedImageData( data )
            Log.i( "imageParsed","Images parsed. Found $numImagesWithNoFaces images with no faces." )
        }
    }


    private fun saveSerializedImageData(data : ArrayList<Pair<String,FloatArray>> ) {
        val serializedDataFile = File(context.filesDir , SERIALIZED_DATA_FILENAME )
        ObjectOutputStream( FileOutputStream( serializedDataFile )  ).apply {
            writeObject( data )
            flush()
            close()
        }
        sharedPreferences.edit().putBoolean( SHARED_PREF_IS_DATA_STORED_KEY , true ).apply()
    }


    private fun loadSerializedImageData() : ArrayList<Pair<String,FloatArray>> {
        val serializedDataFile = File(context.filesDir , SERIALIZED_DATA_FILENAME )
        val objectInputStream = ObjectInputStream(FileInputStream( serializedDataFile ) )
        val data = objectInputStream.readObject() as ArrayList<Pair<String,FloatArray>>
        objectInputStream.close()
        return data
    }

    // Open File chooser to choose the images directory.
    private fun showSelectDirectoryDialog() {
        AlertHelper.serializedFaces(context,
            {
                launchChooseDirectoryIntent()

                Log.d( "serialized","Serialized data loaded.")
            },
            {
                frameAnalyser.faceList = loadSerializedImageData()
            }
        )
        val alertDialog = AlertDialog.Builder( context ).apply {
            setTitle( "Select Images Directory")
            setMessage( "As mentioned in the project\'s README file, please select a directory which contains the images." )
            setCancelable( false )
            setPositiveButton( "SELECT") { dialog, which ->
                dialog.dismiss()
                launchChooseDirectoryIntent()
            }
            create()
        }
        alertDialog.show()
    }

    fun loadListFaces(){
        sharedPreferences = context.getSharedPreferences(context.getString( R.string.app_name ) , Context.MODE_PRIVATE )
        isSerializedDataStored = sharedPreferences.getBoolean( SHARED_PREF_IS_DATA_STORED_KEY , false )
        if ( !isSerializedDataStored ) {
            Log.d("serialized", "No serialized data was found. Select the images directory.")
            showSelectDirectoryDialog()
        }
        else {
            AlertHelper.internetAvailable(context,
                {
                    Toast.makeText(context, "OK CONFIRM", Toast.LENGTH_SHORT).show()
                },
                {
                    Log.d("test", "NO")
                }
            )
        }
    }

}