package com.yudas1337.recognizeface.network

open class Value {

    var value: String? = null
        internal set
    var message: String? = null
        internal set
    var result: List<Result>? = null
        internal set
    var isError: Boolean = false
}