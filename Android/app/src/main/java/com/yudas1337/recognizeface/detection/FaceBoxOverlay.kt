package com.yudas1337.recognizeface.detection

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import kotlin.math.ceil
import kotlin.math.min

open class FaceBoxOverlay(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

    private val lock = Any()
    private val faceBoxes: MutableList<FaceBox> = mutableListOf()
    var mScale: Float? = null
    var mOffsetX: Float? = null
    var mOffsetY: Float? = null

    abstract class FaceBox(private val overlay: FaceBoxOverlay) {

        abstract fun draw(canvas: Canvas?)

        fun getBoxRect(imageRectWidth: Float, imageRectHeight: Float, faceBoundingBox: Rect): RectF {
            val scaleX = overlay.width.toFloat() / imageRectWidth
            val scaleY = overlay.height.toFloat() / imageRectHeight
            val scale = min(scaleX, scaleY)

            overlay.mScale = scale

            val offsetX = (overlay.width.toFloat() - ceil(imageRectWidth * scale)) / 2.0f
            val offsetY = (overlay.height.toFloat() - ceil(imageRectHeight * scale)) / 2.0f

            overlay.mOffsetX = offsetX
            overlay.mOffsetY = offsetY

            val mappedBox = RectF().apply {
                left = overlay.width.toFloat() - (faceBoundingBox.left * scale + offsetX)
                right = overlay.width.toFloat() - (faceBoundingBox.right * scale + offsetX)
                top = faceBoundingBox.top * scale + offsetY
                bottom = faceBoundingBox.bottom * scale + offsetY
            }

            return mappedBox
        }
    }

    fun clear() {
        synchronized(lock) { faceBoxes.clear() }
        postInvalidate()
    }

    fun add(faceBox: FaceBox) {
        synchronized(lock) { faceBoxes.add(faceBox) }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        synchronized(lock) {
            for (graphic in faceBoxes) {
                graphic.draw(canvas)
            }
        }
    }

}
