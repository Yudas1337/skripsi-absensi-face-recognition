package com.yudas1337.recognizeface.detection

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import com.google.mlkit.vision.face.Face

class FaceBox(
    overlay: FaceBoxOverlay,
    private val face: Face,
    private val imageRect: Rect
) : FaceBoxOverlay.FaceBox(overlay) {



    private val paint = Paint().apply {
        color = defaultColor
        style = Paint.Style.STROKE
        strokeWidth = 2.0f
    }

    override fun draw(canvas: Canvas?) {
        val rect = getBoxRect(
            imageRectWidth = imageRect.width().toFloat(),
            imageRectHeight = imageRect.height().toFloat(),
            faceBoundingBox = face.boundingBox
        )

        canvas?.drawRect(rect, paint)
    }

    companion object{
        var defaultColor: Int = Color.GREEN
        fun updateColor(confidence: Float, threshold: Float) {
            defaultColor = if (confidence > threshold) {
                Color.GREEN
            } else {
                Color.RED
            }
        }
    }

}