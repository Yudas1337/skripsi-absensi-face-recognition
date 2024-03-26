package com.yudas1337.recognizeface.recognize

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import com.yudas1337.recognizeface.constants.FaceStatus
import com.yudas1337.recognizeface.recognize.model.FaceNetModel
import com.yudas1337.recognizeface.recognize.model.MaskDetectionModel
import kotlinx.coroutines.withContext
import kotlin.math.pow
import kotlin.math.sqrt

class FrameAnalyser( context: Context ,
                     private var model: FaceNetModel
) {

    private val nameScoreHashmap = HashMap<String,ArrayList<Float>>()
    private var subject = FloatArray( model.embeddingDim )

    // Store the face embeddings in a ( String , FloatArray ) ArrayList.
    // Where String -> name of the person and FloatArray -> Embedding of the face.
    var faceList = ArrayList<Pair<String,FloatArray>>()

    private val maskDetectionModel = MaskDetectionModel( context )
    private var t1 : Long = 0L

    // <-------------- User controls --------------------------->

    // Use any one of the two metrics, "cosine" or "l2"
    private val metricToBeUsed = "cosine"

    // Use this variable to enable/disable mask detection.
    private val isMaskDetectionOn = true

    // <-------------------------------------------------------->

      suspend fun runModel(cameraFrameBitmap: Bitmap): String {
        var output = ""
        withContext( CustomDispatcher.dispatcher ) {

            t1 = System.currentTimeMillis()
            try {
                // Crop the frame using face.boundingBox.
                // Convert the cropped Bitmap to a ByteBuffer.
                // Finally, feed the ByteBuffer to the FaceNet model.
                subject = model.getFaceEmbedding(cameraFrameBitmap)

                // Perform face mask detection on the cropped frame Bitmap.
                var maskLabel = ""
                if ( isMaskDetectionOn ) {
                    maskLabel = maskDetectionModel.detectMask(cameraFrameBitmap)
                }

                // Continue with the recognition if the user is not wearing a face mask
                if (maskLabel == maskDetectionModel.NO_MASK) {
                    // Perform clustering ( grouping )
                    // Store the clusters in a HashMap. Here, the key would represent the 'name'
                    // of that cluster and ArrayList<Float> would represent the collection of all
                    // L2 norms/ cosine distances.
                    for ( i in 0 until faceList.size ) {

                        // If this cluster ( i.e an ArrayList with a specific key ) does not exist,
                        // initialize a new one.
                        if ( nameScoreHashmap[ faceList[ i ].first ] == null ) {
                            // Compute the L2 norm and then append it to the ArrayList.
                            val p = ArrayList<Float>()
                            if ( metricToBeUsed == "cosine" ) {
                                p.add( cosineSimilarity( subject , faceList[ i ].second ) )
                            }
                            else {
                                p.add( L2Norm( subject , faceList[ i ].second ) )
                            }
                            nameScoreHashmap[ faceList[ i ].first ] = p
                        }
                        // If this cluster exists, append the L2 norm/cosine score to it.
                        else {
                            if ( metricToBeUsed == "cosine" ) {
                                nameScoreHashmap[ faceList[ i ].first ]?.add( cosineSimilarity( subject , faceList[ i ].second ) )
                            }
                            else {
                                nameScoreHashmap[ faceList[ i ].first ]?.add( L2Norm( subject , faceList[ i ].second ) )
                            }
                        }
                    }


                    Log.d("wajahnya", "score hashmap nya $nameScoreHashmap")

                    // Compute the average of all scores norms for each cluster.
                    val avgScores = nameScoreHashmap.values.map{ scores ->
                        Log.d("wajahnya", "rata rata nya adalah ${scores.toFloatArray().average()}")
                        scores.toFloatArray().average()
                    }

                    Log.d("wajahnya", "list wajah $avgScores")

                    val names = nameScoreHashmap.keys.toTypedArray()
                    nameScoreHashmap.clear()

                    // Calculate the minimum L2 distance from the stored average L2 norms.
                    val bestScoreUserName: String = if ( metricToBeUsed == "cosine" ) {
                        // In case of cosine similarity, choose the highest value.
                        if ( avgScores.maxOrNull()!! > model.model.cosineThreshold ) {
                            names[ avgScores.indexOf( avgScores.maxOrNull()!! ) ]
                        }
                        else {
                           FaceStatus.UNKNOWN
                        }
                    } else {
                        // In case of L2 norm, choose the lowest value.
                        if ( avgScores.minOrNull()!! > model.model.l2Threshold ) {
                            FaceStatus.UNKNOWN
                        }
                        else {
                            names[ avgScores.indexOf( avgScores.minOrNull()!! ) ]
                        }
                    }

                    output = bestScoreUserName
                }
                else {
                    output = FaceStatus.MASKED
                }
            }
            catch ( e : Exception ) {
                Log.e( "exception" , "Exception in FrameAnalyser : ${e.message}" )
            }

        }
         return output
    }


    // Compute the L2 norm of ( x2 - x1 )
    private fun L2Norm( x1 : FloatArray, x2 : FloatArray ) : Float {
        return sqrt( x1.mapIndexed{ i , xi -> (xi - x2[ i ]).pow( 2 ) }.sum() )
    }


    // Compute the cosine of the angle between x1 and x2.
    private fun cosineSimilarity( x1 : FloatArray , x2 : FloatArray ) : Float {
        // mag 1 = akar pangkat 2 kemudian dijumlah
        val mag1 = sqrt( x1.map { it * it }.sum() )

        // mag 2 = akar pangkat 2 kemudian dijumlah
        val mag2 = sqrt( x2.map { it * it }.sum() )

        // maping tiap index dengan nilai x1 * tiap nilai index dari x2, kemudian dijumlahkan
        val dot = x1.mapIndexed{ index, value -> value * x2[ index ] }.sum()

        // hasil akhir dimasukkan ke dalam array
        // misal 0.7886003
        // [0.7886003, 0.7892291, 0.8033318, dst.. sebanyak jumlah gambar]
        return dot / (mag1 * mag2)

    }

}