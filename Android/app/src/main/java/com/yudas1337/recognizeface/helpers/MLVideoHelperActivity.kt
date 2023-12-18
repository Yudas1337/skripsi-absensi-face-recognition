package com.yudas1337.recognizeface.helpers


import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageFormat
import android.graphics.Rect
import android.graphics.YuvImage
import android.media.Image
import android.os.Build
import android.os.Bundle
import android.view.Surface
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.OptIn
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.AspectRatio
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.camera.view.PreviewView.*
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.google.common.util.concurrent.ListenableFuture
import com.yudas1337.recognizeface.R
import com.yudas1337.recognizeface.helpers.vision.GraphicOverlay
import com.yudas1337.recognizeface.helpers.vision.VisionBaseProcessor
import java.io.ByteArrayOutputStream
import java.util.concurrent.ExecutionException
import java.util.concurrent.Executor
import java.util.concurrent.Executors


abstract class MLVideoHelperActivity : AppCompatActivity() {
    protected var previewView: PreviewView? = null
    protected var graphicOverlay: GraphicOverlay? = null
    private var outputTextView: TextView? = null
    private var addFaceButton: ImageView? = null
    private var cameraProviderFuture: ListenableFuture<ProcessCameraProvider>? = null
    private val executor: Executor = Executors.newSingleThreadExecutor()
    private var processor: VisionBaseProcessor<Unit>? = null
    private var imageAnalysis: ImageAnalysis? = null
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_helper)
        previewView = findViewById(R.id.camera_source_preview)
        graphicOverlay = findViewById(R.id.graphic_overlay)
        outputTextView = findViewById<TextView>(R.id.output_text_view)
        addFaceButton = findViewById<ImageView>(R.id.button_add_face)
        cameraProviderFuture = ProcessCameraProvider.getInstance(applicationContext)
        processor = setProcessor()
        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.CAMERA), REQUEST_CAMERA)
        } else {
            initSource()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        processor?.stop()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CAMERA && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            initSource()
        }
    }

    protected fun setOutputText(text: String?) {
        outputTextView!!.text = text
    }

    private fun initSource() {
        cameraProviderFuture!!.addListener(
            {
                try {
                    val cameraProvider: ProcessCameraProvider = cameraProviderFuture!!.get()
                    bindPreview(cameraProvider)
                } catch (e: ExecutionException) {
                    // No errors need to be handled for this Future.
                    // This should never be reached.
                } catch (e: InterruptedException) {
                }
            }, ContextCompat.getMainExecutor(
                applicationContext
            )
        )
    }

    fun bindPreview(cameraProvider: ProcessCameraProvider) {
        val lensFacing = lensFacing
        val preview: Preview = Preview.Builder()
            .setTargetAspectRatio(AspectRatio.RATIO_4_3)
            .build()
        preview.setSurfaceProvider(previewView!!.getSurfaceProvider())
        val cameraSelector: CameraSelector = CameraSelector.Builder()
            .requireLensFacing(lensFacing)
            .build()
        imageAnalysis = ImageAnalysis.Builder()
            .setTargetAspectRatio(AspectRatio.RATIO_4_3)
            .build()
        setFaceDetector(lensFacing)
        cameraProvider.bindToLifecycle(this, cameraSelector, imageAnalysis, preview)
    }

    /**
     * The face detector provides face bounds whose coordinates, width and height depend on the
     * preview's width and height, which is guaranteed to be available after the preview starts
     * streaming.
     */
    private fun setFaceDetector(lensFacing: Int) {
        previewView!!.getPreviewStreamState().observe(this, object : Observer<StreamState?> {
            override fun onChanged(value: StreamState?) {
                if (value !== StreamState.STREAMING) {
                    return
                }
                val preview: View = previewView!!.getChildAt(0)
                var width = preview.width * preview.scaleX
                var height = preview.height * preview.scaleY
                val rotation = preview.display.rotation.toFloat()
                if (rotation == Surface.ROTATION_90.toFloat() || rotation == Surface.ROTATION_270.toFloat()) {
                    val temp = width
                    width = height
                    height = temp
                }
                imageAnalysis?.setAnalyzer(
                    executor,
                    createFaceDetector(width.toInt(), height.toInt(), lensFacing)
                )
                previewView?.previewStreamState?.removeObserver(this)
            }
        })
    }

    @OptIn(markerClass = arrayOf(ExperimentalGetImage::class))
    private fun createFaceDetector(
        width: Int,
        height: Int,
        lensFacing: Int
    ): ImageAnalysis.Analyzer {
        graphicOverlay!!.setPreviewProperties(width, height, lensFacing)
        return ImageAnalysis.Analyzer { imageProxy ->
            if (imageProxy.getImage() == null) {
                imageProxy.close()
                return@Analyzer
            }
            val rotationDegrees: Int = imageProxy.getImageInfo().getRotationDegrees()
            // converting from YUV format
            processor!!.detectInImage(imageProxy, toBitmap(imageProxy.getImage()!!), rotationDegrees)
            // after done, release the ImageProxy object
            imageProxy.close()
        }
    }

    private fun toBitmap(image: Image): Bitmap {
        val planes = image.planes
        val yBuffer = planes[0].buffer
        val uBuffer = planes[1].buffer
        val vBuffer = planes[2].buffer
        val ySize = yBuffer.remaining()
        val uSize = uBuffer.remaining()
        val vSize = vBuffer.remaining()
        val nv21 = ByteArray(ySize + uSize + vSize)
        //U and V are swapped
        yBuffer[nv21, 0, ySize]
        vBuffer[nv21, ySize, vSize]
        uBuffer[nv21, ySize + vSize, uSize]
        val yuvImage = YuvImage(nv21, ImageFormat.NV21, image.width, image.height, null)
        val out = ByteArrayOutputStream()
        yuvImage.compressToJpeg(Rect(0, 0, yuvImage.width, yuvImage.height), 75, out)
        val imageBytes = out.toByteArray()
        return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
    }

    protected val lensFacing: Int
        protected get() = CameraSelector.LENS_FACING_BACK

    protected abstract fun setProcessor(): VisionBaseProcessor<Unit>?
    fun makeAddFaceVisible() {
        addFaceButton!!.visibility = View.VISIBLE
    }

    fun onAddFaceClicked(view: View?) {}

    companion object {
        private const val REQUEST_CAMERA = 1001
    }
}