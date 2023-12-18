package com.yudas1337.recognizeface.helpers

import java.util.Date
import java.util.UUID

class PackageHelper {

    companion object{
         val timestamp = Date().toString()
         val uuid = UUID.randomUUID().toString()
    }
}