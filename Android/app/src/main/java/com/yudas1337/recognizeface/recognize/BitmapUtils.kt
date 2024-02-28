package com.yudas1337.recognizeface.recognize

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.Context
import android.graphics.*
import android.media.Image
import android.net.Uri
import android.os.ParcelFileDescriptor
import androidx.camera.core.ImageProxy
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileDescriptor
import java.io.FileOutputStream
import java.nio.ByteBuffer

class BitmapUtils {

    companion object {

        fun frameToImageBytes(byteArray: ByteArray, previewWidth: Int, previewHeight: Int): ByteArray {
            val yuvImage = YuvImage(byteArray, ImageFormat.NV21, previewWidth, previewHeight, null)
            val out = ByteArrayOutputStream()
            yuvImage.compressToJpeg(Rect(0, 0, previewWidth, previewHeight), 100, out)

            return out.toByteArray()
        }

        // Crop the given bitmap with the given rect.
        fun cropRectFromBitmap(source: Bitmap, rect: Rect ): Bitmap {
            var width = rect.width()
            var height = rect.height()
            if ( (rect.left + width) > source.width ){
                width = source.width - rect.left
            }
            if ( (rect.top + height ) > source.height ){
                height = source.height - rect.top
            }

            return Bitmap.createBitmap( source , rect.left , rect.top , width , height )

        }


        // Get the image as a Bitmap from given Uri
        // Source -> https://developer.android.com/training/data-storage/shared/documents-files#bitmap
        fun getBitmapFromUri( contentResolver : ContentResolver , uri: Uri): Bitmap {
            val parcelFileDescriptor: ParcelFileDescriptor? = contentResolver.openFileDescriptor(uri, "r")
            val fileDescriptor: FileDescriptor = parcelFileDescriptor!!.fileDescriptor
            val image: Bitmap = BitmapFactory.decodeFileDescriptor(fileDescriptor)
            parcelFileDescriptor.close()
            return image
        }


        // Rotate the given `source` by `degrees`.
        // See this SO answer -> https://stackoverflow.com/a/16219591/10878733
        fun rotateBitmap( source: Bitmap , degrees : Float ): Bitmap {
            val matrix = Matrix()
            matrix.postRotate( degrees )
            return Bitmap.createBitmap(source, 0, 0, source.width, source.height, matrix , false )
        }


        // Flip the given `Bitmap` horizontally.
        // See this SO answer -> https://stackoverflow.com/a/36494192/10878733
        fun flipBitmap( source: Bitmap ): Bitmap {
            val matrix = Matrix()
            matrix.postScale(-1f, 1f, source.width / 2f, source.height / 2f)
            return Bitmap.createBitmap(source, 0, 0, source.width, source.height, matrix, true)
        }


        // Use this method to save a Bitmap to the internal storage ( app-specific storage ) of your device.
        // To see the image, go to "Device File Explorer" -> "data" -> "data" -> "com.ml.quaterion.facenetdetection" -> "files"
        fun saveBitmap(context: Context, image: Bitmap, name: String) {
            val fileOutputStream = FileOutputStream(File( context.filesDir.absolutePath + "/$name.png"))
            image.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream)
        }

        // Convert android.media.Image to android.graphics.Bitmap
        // See the SO answer -> https://stackoverflow.com/a/44486294/10878733
        fun imageToBitmap( image : Image , rotationDegrees : Int ): Bitmap {
            val yBuffer = image.planes[0].buffer
            val uBuffer = image.planes[1].buffer
            val vBuffer = image.planes[2].buffer
            val ySize = yBuffer.remaining()
            val uSize = uBuffer.remaining()
            val vSize = vBuffer.remaining()
            val nv21 = ByteArray(ySize + uSize + vSize)
            yBuffer.get(nv21, 0, ySize)
            vBuffer.get(nv21, ySize, vSize)
            uBuffer.get(nv21, ySize + vSize, uSize)
            val yuvImage = YuvImage(nv21, ImageFormat.NV21, image.width, image.height, null)
            val out = ByteArrayOutputStream()
            yuvImage.compressToJpeg(Rect(0, 0, yuvImage.width, yuvImage.height), 100, out)
            val yuv = out.toByteArray()
            var output = BitmapFactory.decodeByteArray(yuv, 0, yuv.size)
            output = rotateBitmap( output , rotationDegrees.toFloat() )
            return flipBitmap( output )
        }

        @SuppressLint("UnsafeOptInUsageError")
        fun imageProxyToBitmap(imageProxy: ImageProxy): Bitmap? {
            val image: Image? = imageProxy.image
            if (image != null) {
                try {
                    val buffer: ByteBuffer = image.planes[0].buffer
                    val bytes = ByteArray(buffer.remaining())
                    buffer.get(bytes)

                    val width: Int = imageProxy.width
                    val height: Int = imageProxy.height

                    // Create Bitmap from the image data
                    val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
                    bitmap.copyPixelsFromBuffer(ByteBuffer.wrap(bytes))

                    return bitmap
                } catch (e: Exception) {
                    e.printStackTrace()
                } finally {
                    imageProxy.close()
                    image.close()
                }
            }
            return null
        }


        // Convert the given Bitmap to NV21 ByteArray
        // See this comment -> https://github.com/firebase/quickstart-android/issues/932#issuecomment-531204396
        suspend fun bitmapToNV21ByteArray(bitmap: Bitmap): ByteArray = withContext(Dispatchers.Default) {
            val argb = IntArray(bitmap.width * bitmap.height )
            bitmap.getPixels(argb, 0, bitmap.width, 0, 0, bitmap.width, bitmap.height)
            val yuv = ByteArray(bitmap.height * bitmap.width + 2 * Math.ceil(bitmap.height / 2.0).toInt()
                    * Math.ceil(bitmap.width / 2.0).toInt())
            encodeYUV420SP( yuv, argb, bitmap.width, bitmap.height)
            return@withContext yuv
        }

        fun nv21ToBitmap(yuv: ByteArray, width: Int, height: Int): Bitmap? {
            val argb = IntArray(width * height)
            decodeYUV420SP(argb, yuv, width, height)

            return Bitmap.createBitmap(argb, width, height, Bitmap.Config.ARGB_8888)
        }

        private fun decodeYUV420SP(argb: IntArray, yuv: ByteArray, width: Int, height: Int) {
            val frameSize = width * height

            var uvp = frameSize
            var u = 0
            var v = 0
            var yIndex = 0

            for (j in 0 until height) {
                var uvpIndex = 0
                val yp = yIndex
                for (i in 0 until width) {
                    var y = (0xff and yuv[yp].toInt()) - 16
                    if (y < 0) y = 0

                    if (i and 1 == 0) {
                        u = (0xff and yuv[uvp + uvpIndex].toInt()) - 128
                        v = (0xff and yuv[uvp + uvpIndex + 1].toInt()) - 128
                        uvpIndex += 2
                    }

                    var y1192 = 1192 * y
                    var r = y1192 + 1634 * v
                    var g = y1192 - 833 * v - 400 * u
                    var b = y1192 + 2066 * u

                    if (r < 0) r = 0 else if (r > 262143) r = 262143
                    if (g < 0) g = 0 else if (g > 262143) g = 262143
                    if (b < 0) b = 0 else if (b > 262143) b = 262143

                    argb[yIndex++] = -0x1000000 or ((r shl 6) and 0xff0000) or ((g shr 2) and 0xff00) or (b shr 10)
                }
                if (j and 1 == 0) {
                    uvp += width
                }
            }
        }

        private fun encodeYUV420SP(yuv420sp: ByteArray, argb: IntArray, width: Int, height: Int) {
            val frameSize = width * height
            var yIndex = 0
            var uvIndex = frameSize
            var R: Int
            var G: Int
            var B: Int
            var Y: Int
            var U: Int
            var V: Int
            var index = 0
            for (j in 0 until height) {
                for (i in 0 until width) {
                    R = argb[index] and 0xff0000 shr 16
                    G = argb[index] and 0xff00 shr 8
                    B = argb[index] and 0xff shr 0
                    Y = (66 * R + 129 * G + 25 * B + 128 shr 8) + 16
                    U = (-38 * R - 74 * G + 112 * B + 128 shr 8) + 128
                    V = (112 * R - 94 * G - 18 * B + 128 shr 8) + 128
                    yuv420sp[yIndex++] = (if (Y < 0) 0 else if (Y > 255) 255 else Y).toByte()
                    if (j % 2 == 0 && index % 2 == 0) {
                        yuv420sp[uvIndex++] = (if (V < 0) 0 else if (V > 255) 255 else V).toByte()
                        yuv420sp[uvIndex++] = (if (U < 0) 0 else if (U > 255) 255 else U).toByte()
                    }
                    index++
                }
            }
        }

    }

}