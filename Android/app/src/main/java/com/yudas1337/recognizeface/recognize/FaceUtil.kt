package com.yudas1337.recognizeface.recognize

import android.content.Context
import com.yudas1337.recognizeface.constants.ModelControl
import com.yudas1337.recognizeface.recognize.model.FaceNetModel


class FaceUtil {

    companion object{
        fun initializeFaceNetModel(context: Context): FaceNetModel {
            return FaceNetModel(
                context,
                ModelControl.modelInfo,
                ModelControl.useGpu,
                ModelControl.useXNNPack
            )
        }

        fun initializeFrameAnalyser(context: Context, faceNetModel: FaceNetModel): FrameAnalyser {
            return FrameAnalyser(context, faceNetModel)
        }
    }



}