package com.yudas1337.recognizeface.helpers

import android.database.Cursor
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Locale

class CalendarHelper {

    companion object{
        @RequiresApi(Build.VERSION_CODES.O)
        // wednesday, friday, monday, etc
        fun getToday(): String {
            return LocalDate.now().format(DateTimeFormatter.ofPattern("EEEE", Locale.ENGLISH)).lowercase()
        }

        @RequiresApi(Build.VERSION_CODES.O)
        // 2024-92-11
        fun getDate(): String{
            return LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        }

        @RequiresApi(Build.VERSION_CODES.O)
        fun convertToLocalTime(time: String): LocalTime{
            return LocalTime.parse(time, DateTimeFormatter.ofPattern("HH:mm:ss"))
        }

        @RequiresApi(Build.VERSION_CODES.O)
        // 08:15:00
        fun getAttendanceHours(): String {
            return LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"))
        }

        @RequiresApi(Build.VERSION_CODES.O)
        // 08:00:00 + max, misal 15 menit
        // jadinya: 08:15:00
        fun addLimitAttendance(cursor: Cursor, maxMinutes: Long): String {
            val checkInEndsId = cursor.getColumnIndex("checkin_ends")
            val checkinEnds = cursor.getString(checkInEndsId)

            return LocalTime.parse(checkinEnds, DateTimeFormatter.ofPattern("HH:mm:ss"))
                .plusMinutes(maxMinutes)
                .format(DateTimeFormatter.ofPattern("HH:mm:ss"))

        }
    }

}