package com.yudas1337.recognizeface.helpers

import java.util.Date
import java.util.UUID

class PackageHelper {

    companion object{
        fun generateUUID(): String {
            return UUID.randomUUID().toString()
        }
    }

}