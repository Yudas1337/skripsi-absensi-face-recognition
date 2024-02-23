package com.yudas1337.recognizeface.recognize

import kotlinx.coroutines.asCoroutineDispatcher
import java.util.concurrent.Executors

object CustomDispatcher {
    val dispatcher = Executors.newFixedThreadPool(1).asCoroutineDispatcher()
}