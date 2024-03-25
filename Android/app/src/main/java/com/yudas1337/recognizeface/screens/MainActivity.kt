@file:Suppress("DEPRECATION")

package com.yudas1337.recognizeface.screens

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageFormat
import android.graphics.Rect
import android.hardware.Camera
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Environment
import android.util.Log
import android.view.SurfaceHolder
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import cn.pedant.SweetAlert.SweetAlertDialog
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.Face
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetectorOptions
import com.yudas1337.recognizeface.DetectionResult
import com.yudas1337.recognizeface.EngineWrapper
import com.yudas1337.recognizeface.SetThresholdDialogFragment
import com.yudas1337.recognizeface.constants.FaceFolder
import com.yudas1337.recognizeface.databinding.ActivityMainBinding
import com.yudas1337.recognizeface.detection.FaceBox
import com.yudas1337.recognizeface.helpers.BrightnessHelper
import com.yudas1337.recognizeface.helpers.PermissionHelper
import com.yudas1337.recognizeface.recognize.BitmapUtils
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.newSingleThreadContext
import java.io.File
import java.io.IOException

@ObsoleteCoroutinesApi
class MainActivity : AppCompatActivity(), SetThresholdDialogFragment.ThresholdDialogListener {
    // https://jpegclub.org/exif_orientation.html

    private lateinit var binding: ActivityMainBinding
    private lateinit var engineWrapper: EngineWrapper

    private var factorX: Float = 0F
    private var factorY: Float = 0F
    private var threshold: Float = defaultThreshold

    private var camera: Camera? = null

    private var enginePrepared: Boolean = false
    private var working: Boolean = false
    private var isReal: Boolean? = null

    private var cameraId: Int = Camera.CameraInfo.CAMERA_FACING_FRONT
    private val previewWidth: Int = 1920
    private val previewHeight: Int = 1440
    private val frameOrientation: Int = 1
    private var screenWidth: Int = 0
    private var screenHeight: Int = 0

    private val detectionContext = newSingleThreadContext("detection")

    private lateinit var timer: CountDownTimer
    private var stableTime: Long = 3000

    private var name: String? = null
    private var id: String? = null
    private var rfid: String? = null
    private var role: String? = null

    lateinit var pDialog: SweetAlertDialog

    private var surfaceCallback: SurfaceHolder.Callback? = null

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (PermissionHelper.hasPermissions(this, permissions)) {
            init()
        } else {
            PermissionHelper.requestPermission(this, permissions, permissionReqCode)
        }

    }

    private fun startTimer(stableTime: Long){
        timer = object: CountDownTimer(stableTime, 1000) {
            override fun onTick(millisUntilFinished: Long) {

            }

            override fun onFinish() {
                //
            }
        }
    }

    private fun resetTimer() {
        timer.cancel()
        stableTime = 3000
        startTimer(stableTime)
    }

    private fun checkFaceBoundingBox(face: Face): Boolean {
        val box = face.boundingBox
        return box.left >= 0 && box.top >= 0 && box.right >= 0 && box.bottom >= 0
    }

    private fun stopCamera() {
        if (camera != null) {
            camera!!.setPreviewCallback(null)
            camera!!.stopPreview()
            camera!!.release()
            camera = null
        }
    }

    private fun saveFace(bitmap: Bitmap): Boolean {

        val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val imageFile = File(downloadsDir, FaceFolder.CROPPED_FACE)

        if(imageFile.exists()){
            imageFile.delete()
        }

        return try {
            BitmapUtils.saveBitmap(bitmap, imageFile)
            true
        } catch (e: Exception) {
            Log.d("", "error gan ${e.message}")
            e.printStackTrace()
            false
        }
    }

    private fun processImageByteArray(data: ByteArray) {
        val inputImage = InputImage.fromByteArray(
            data,
            previewWidth,
            previewHeight,
            0,
            InputImage.IMAGE_FORMAT_NV21
        )

        val detector = FaceDetection.getClient(
            FaceDetectorOptions.Builder()
                .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_FAST)
                .setContourMode(FaceDetectorOptions.CONTOUR_MODE_NONE)
                .build())

        // Proses deteksi wajah
        detector.process(inputImage)
            .addOnSuccessListener { faces ->

                runOnUiThread {
                    binding.graphicOverlay.clear()
                    if (faces.isNotEmpty()) {
                        // mendeteksi wajah hanya dan diharuskan 1 saja
                        if(faces.size == 1){
                            val face = faces[0]
                            val faceBox = FaceBox(binding.graphicOverlay, face, Rect(0, 0, previewWidth, previewHeight))
                            binding.graphicOverlay.add(faceBox)

                            if (stableTime.toInt() == 3000) {
                                timer.start()
                            }

                            stableTime -= 1000

                            // cek liveness
                            if(isReal == true){
                                binding.timeText.visibility = View.VISIBLE

                                // cek timer
                                if(stableTime <= 0){

                                    if(checkFaceBoundingBox(face)){
                                        binding.facePositionText.text = "Wajah Terdeteksi.."
                                        binding.timeText.visibility = View.VISIBLE

                                        val imageBytes = BitmapUtils.frameToImageBytes(data, previewWidth, previewHeight)
                                        val bitmap = BitmapUtils.cropRectFromBitmap(BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size), face.boundingBox)

                                        ScanActivity.croppedBitmap = bitmap
                                        setResult(RESULT_OK, Intent())
                                        finish()

//                                        if(saveFace(bitmap)){
//                                            destroySurfaceCallback()
//                                            stopCamera()
//                                            setResult(RESULT_OK, Intent())
//                                            finish()
//                                        }

                                    } else{
                                        binding.facePositionText.text = "Anda berada di luar frame deteksi"
                                        binding.timeText.visibility = View.GONE
                                    }

                                }
                            } else{
                                resetTimer()
                                binding.facePositionText.text = "Spoofing Terdeteksi"
                                binding.timeText.visibility = View.GONE
                            }

                        }

                    } else{
                        binding.facePositionText.text = "Wajah tidak Terdeteksi"
                        resetTimer()
                        binding.timeText.visibility = View.GONE
                    }
                }

                binding.graphicOverlay.postInvalidate()
            }
            .addOnFailureListener { e ->
                e.printStackTrace()
            }
    }

    private fun destroySurfaceCallback() {
        binding.surface.holder.removeCallback(surfaceCallback)
        surfaceCallback = null
    }

    @RequiresApi(Build.VERSION_CODES.M)
    @OptIn(DelicateCoroutinesApi::class)
    private fun init() {
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.result = DetectionResult()

        binding.facePositionText.text = facePositionText

        rfid = intent.getStringExtra("rfid")
        name = intent.getStringExtra("name")
        id = intent.getStringExtra("id")
        role = intent.getStringExtra("role")

        startTimer(stableTime)

        BrightnessHelper.checkBrightnessPermission(this, 255)

        // Inisialisasi callback Anda
        surfaceCallback = object : SurfaceHolder.Callback, Camera.PreviewCallback {
            @Deprecated("Deprecated in Java")
            override fun onPreviewFrame(data: ByteArray?, camera: Camera?) {

                if(data != null){
                    processImageByteArray(data)
                }

                if (enginePrepared && data != null) {
                    if (!working) {
                        GlobalScope.launch(detectionContext) {
                            working = true
                            val result = engineWrapper.detect(
                                data,
                                previewWidth,
                                previewHeight,
                                frameOrientation
                            )

                            result.threshold = threshold

                            if(result.confidence > result.threshold){
                                isReal = true
                                FaceBox.updateColor(isReal!!)
                            } else{
                                isReal = false
                                FaceBox.updateColor(isReal!!)
                            }
                            working = false
                        }
                    }
                }
            }

            override fun surfaceCreated(p0: SurfaceHolder) {
                try {
                    camera = Camera.open(cameraId)
                } catch (e: Exception) {
                    cameraId = Camera.CameraInfo.CAMERA_FACING_FRONT
                    camera = Camera.open(cameraId)
                }

                try {
                    camera!!.setPreviewDisplay(binding.surface.holder)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }

            override fun surfaceChanged(p0: SurfaceHolder, p1: Int, p2: Int, p3: Int) {

                if (p0.surface == null) return

                if (camera == null) return

                try {
                    camera?.stopPreview()
                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                }

                val parameters = camera?.parameters

                parameters?.setPreviewSize(previewWidth, previewHeight)

                factorX = screenWidth / previewHeight.toFloat()
                factorY = screenHeight / previewWidth.toFloat()

                camera?.parameters = parameters

                camera?.startPreview()
                camera?.setPreviewCallback(this)

            }

            override fun surfaceDestroyed(p0: SurfaceHolder) {
                camera?.setPreviewCallback(null)
                camera!!.stopPreview()
                camera?.release()
                camera = null
            }
        }

        // Tambahkan callback ke SurfaceHolder
        binding.surface.holder.setFormat(ImageFormat.NV21)

        binding.surface.holder.addCallback(surfaceCallback)
    }

    fun setting(@Suppress("UNUSED_PARAMETER") view: View) =
        SetThresholdDialogFragment().show(supportFragmentManager, "dialog")

    override fun onDialogPositiveClick(t: Float) {
        threshold = t
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == permissionReqCode) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                init()
            } else {
                Toast.makeText(this, "Permission Denied. Failed to Launch Camera", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onResume() {
        engineWrapper = EngineWrapper(assets)
        enginePrepared = engineWrapper.init()

        if (!enginePrepared) {
            Toast.makeText(this, "Engine init failed.", Toast.LENGTH_LONG).show()
        }

        super.onResume()
    }

    override fun onDestroy() {
        engineWrapper.destroy()
        super.onDestroy()
    }

    companion object {
        //      const val defaultThreshold = 0.915F
        const val defaultThreshold = 0.60F
        const val facePositionText = "Posisikan Wajah ke tengah di Dalam Frame"
        val permissions: Array<String> = arrayOf(Manifest.permission.CAMERA)
        const val permissionReqCode = 1
    }

}