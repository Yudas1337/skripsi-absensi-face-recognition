package com.yudas1337.recognizeface.recognize

import android.graphics.Rect

data class Prediction( var bbox : Rect, var label : String , var maskLabel : String = "" )