package com.yudas1337.recognizeface.helpers

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class CalendarHelper {

    @RequiresApi(Build.VERSION_CODES.O)
    fun getToday(): String {
        return LocalDate.now().format(DateTimeFormatter.ofPattern("EEEE")).lowercase()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getAttendanceHours(): String {
        return LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"))
    }
}