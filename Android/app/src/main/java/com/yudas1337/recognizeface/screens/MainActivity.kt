@file:Suppress("DEPRECATION")

package com.yudas1337.recognizeface.screens

import android.Manifest
import android.annotation.TargetApi
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageFormat
import android.graphics.Rect
import android.graphics.YuvImage
import android.hardware.Camera
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Environment
import android.util.Log
import android.view.SurfaceHolder
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.Face
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetectorOptions
import com.yudas1337.recognizeface.DetectionResult
import com.yudas1337.recognizeface.EngineWrapper
import com.yudas1337.recognizeface.SetThresholdDialogFragment
import com.yudas1337.recognizeface.constants.ConstShared
import com.yudas1337.recognizeface.constants.ModelControl
import com.yudas1337.recognizeface.databinding.ActivityMainBinding
import com.yudas1337.recognizeface.detection.FaceBox
import com.yudas1337.recognizeface.recognize.BitmapUtils
import com.yudas1337.recognizeface.recognize.FrameAnalyser
import com.yudas1337.recognizeface.recognize.LoadFace
import com.yudas1337.recognizeface.recognize.model.FaceNetModel
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.newSingleThreadContext
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream


@ObsoleteCoroutinesApi
class MainActivity : AppCompatActivity(), SetThresholdDialogFragment.ThresholdDialogListener {

    private lateinit var binding: ActivityMainBinding

    private var enginePrepared: Boolean = false
    private lateinit var engineWrapper: EngineWrapper
    private var threshold: Float = defaultThreshold

    private var camera: Camera? = null
    private var cameraId: Int = Camera.CameraInfo.CAMERA_FACING_FRONT

    private val previewWidth: Int = 1920
    private val previewHeight: Int = 1440

    // https://jpegclub.org/exif_orientation.html

    private val frameOrientation: Int = 1

    private var screenWidth: Int = 0
    private var screenHeight: Int = 0

    private var factorX: Float = 0F
    private var factorY: Float = 0F

    private val detectionContext = newSingleThreadContext("detection")
    private var working: Boolean = false

    private lateinit var timer: CountDownTimer
    private var stableTime: Long = 3000

    private var isReal: Boolean? = null

    private lateinit var frameAnalyser  : FrameAnalyser
    private lateinit var faceNetModel : FaceNetModel
    private lateinit var loadFace: LoadFace

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (hasPermissions()) {
            init()
        } else {
            requestPermission()
        }

    }

    private fun hasPermissions(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (permission in permissions) {
                if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                    return false
                }
            }
        }
        return true
    }

    @TargetApi(Build.VERSION_CODES.M)
    private fun requestPermission() = requestPermissions(permissions, permissionReqCode)

    private fun startTimer(){
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

    }

    private fun checkFaceBoundingBox(face: Face): Boolean {
        val box = face.boundingBox
        return box.left >= 0 && box.top >= 0 && box.right >= 0 && box.bottom >= 0
    }

    private fun saveFace(byteArray: ByteArray, fileName: String, face: Face): Boolean {

        val yuvImage = YuvImage(byteArray, ImageFormat.NV21, previewWidth, previewHeight, null)
        val out = ByteArrayOutputStream()
        yuvImage.compressToJpeg(Rect(0, 0, previewWidth, previewHeight), 100, out)
        val imageBytes = out.toByteArray()


        if(checkFaceBoundingBox(face)){
            binding.facePositionText.text = "Wajah Terdeteksi.."
            binding.timeText.visibility = View.VISIBLE

            val bitmap = BitmapUtils.cropRectFromBitmap(BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size), face.boundingBox)

            val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            val imageFile = File(downloadsDir, fileName)

            return try {
                val outputStream: OutputStream = FileOutputStream(imageFile, false)
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
                outputStream.write(imageBytes)
                outputStream.flush()
                outputStream.close()

                true
            } catch (e: Exception) {
                Log.d("wajahnya", "error gan ${e.message}")
                e.printStackTrace()
                false
            }

        } else{
            binding.facePositionText.text = "Anda berada di luar frame deteksi"
            binding.timeText.visibility = View.GONE
        }
        return false
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
                                    saveFace(data, "cropped_face.png", face)

//                                CoroutineScope( Dispatchers.Default ).launch {
//                                    frameAnalyser.runModel(face, bitmap)
//                                }


                                }
                            } else{
//                                resetTimer()
                                binding.facePositionText.text = "Spoofing Terdeteksi"
                                binding.timeText.visibility = View.GONE
                            }

                        }

                    } else{
                        binding.facePositionText.text = "Wajah tidak Terdeteksi"
//                        resetTimer()
                        binding.timeText.visibility = View.GONE
                    }
                }

                binding.graphicOverlay.postInvalidate()
            }
            .addOnFailureListener { e ->
                e.printStackTrace()
            }
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun init() {
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.result = DetectionResult()

        faceNetModel = FaceNetModel( this , ModelControl.modelInfo , ModelControl.useGpu , ModelControl.useXNNPack )
        frameAnalyser = FrameAnalyser(this, faceNetModel)

        loadFace = LoadFace(this, frameAnalyser, getSharedPreferences(ConstShared.fileName, MODE_PRIVATE))
        frameAnalyser.faceList = loadFace.loadSerializedImageData()

        binding.facePositionText.text = facePositionText

        startTimer()

        binding.surface.holder.let {
            it.setFormat(ImageFormat.NV21)
            it.addCallback(object : SurfaceHolder.Callback, Camera.PreviewCallback {

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
                    camera?.release()
                    camera = null
                }
            })
        }
    }

    fun setting(@Suppress("UNUSED_PARAMETER") view: View) =
        SetThresholdDialogFragment().show(supportFragmentManager, "dialog")

    override fun onDialogPositiveClick(t: Float) {
        threshold = t
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == permissionReqCode) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                init()
            } else {
                Toast.makeText(this, "Permission Not Granted", Toast.LENGTH_LONG).show()
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