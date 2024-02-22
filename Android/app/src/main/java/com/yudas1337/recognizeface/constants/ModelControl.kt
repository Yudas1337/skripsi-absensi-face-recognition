package com.yudas1337.recognizeface.constants

import com.yudas1337.recognizeface.recognize.model.Models

object ModelControl {

    // <----------------------- User controls --------------------------->

    // Use the device's GPU to perform faster computations.
    // Refer https://www.tensorflow.org/lite/performance/gpu
    const val useGpu = true

    // Use XNNPack to accelerate inference.
    // Refer https://blog.tensorflow.org/2020/07/accelerating-tensorflow-lite-xnnpack-integration.html
    const val useXNNPack = true

    // Default is Models.FACENET ; Quantized models are faster
    val modelInfo = Models.FACENET

    // <---------------------------------------------------------------->
}