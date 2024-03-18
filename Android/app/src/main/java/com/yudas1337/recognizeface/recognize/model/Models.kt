package com.yudas1337.recognizeface.recognize.model

class Models {

    companion object {

        // rencana dibuat 0.6, atau 0.7
        val FACENET = ModelInfo(
            "FaceNet" ,
            "facenet.tflite" ,
            0.5f ,
            10f ,
            128 ,
            160
        )

        val FACENET_512 = ModelInfo(
            "FaceNet-512" ,
            "facenet_512.tflite" ,
            0.3f ,
            23.56f ,
            512 ,
            160
        )

        val FACENET_QUANTIZED = ModelInfo(
            "FaceNet Quantized" ,
            "facenet_int_quantized.tflite" ,
            0.4f ,
            10f ,
            128 ,
            160
        )

        val FACENET_512_QUANTIZED = ModelInfo(
            "FaceNet-512 Quantized" ,
            "facenet_512_int_quantized.tflite" ,
            0.3f ,
            23.56f ,
            512 ,
            160
        )


    }

}