package com.yudas1337.recognizeface.services

import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.os.Build
import androidx.annotation.RequiresApi
import com.yudas1337.recognizeface.constants.AttendanceStatus
import com.yudas1337.recognizeface.constants.Role
import com.yudas1337.recognizeface.database.DBHelper
import com.yudas1337.recognizeface.helpers.AlertHelper
import com.yudas1337.recognizeface.helpers.CalendarHelper
import com.yudas1337.recognizeface.helpers.VoiceHelper
import com.yudas1337.recognizeface.screens.MainActivity
import kotlinx.coroutines.ObsoleteCoroutinesApi
import java.time.LocalTime

class ScanService(private val context: Context, private val dbHelper: DBHelper, private val voiceHelper: VoiceHelper) {


    fun handleScan(rfid: String): HashMap<String, String?> {
        val result = HashMap<String, String?>()

        val students = dbHelper.findUsersByRfid("students", rfid)
        val employees = dbHelper.findUsersByRfid("employees", rfid)

        if (students != null && students.moveToFirst()) {
            val indexName = students.getColumnIndex("name")
            val name = students.getString(indexName)
            val indexId = students.getColumnIndex("id")
            val id = students.getString(indexId)

            students.close()
            dbHelper.close()

            result["rfid"] = rfid
            result["name"] = name
            result["id"] = id
            result["role"] = Role.STUDENT

        } else if(employees != null && employees.moveToFirst()) {
            val indexName = employees.getColumnIndex("name")
            val name = employees.getString(indexName)
            val indexId = employees.getColumnIndex("uuid")
            val id = employees.getString(indexId)

            employees.close()
            dbHelper.close()

            result["rfid"] = rfid
            result["name"] = name
            result["id"] = id
            result["role"] = Role.EMPLOYEE

        }

        return result

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getCurrentAttendance(id: String, dbHelper: DBHelper): Cursor {
        return dbHelper.getTodayAttendance(id, CalendarHelper.getDate())
    }

    @RequiresApi(Build.VERSION_CODES.O)
     fun handleAttendances(id: String, role: String, name: String): Boolean{
        val day = dbHelper.getScheduleDay(CalendarHelper.getToday())

        if(day.moveToFirst()){
            val getLimit = dbHelper.getAttendanceLimit()?.let { CalendarHelper.addLimitAttendance(day, it) }
            var attendance = getCurrentAttendance(id, dbHelper)

            // cek apakah belum absen masuk hari ini di pagi hari
            if(!attendance.moveToFirst()){
//                 cek apakah dia telat atau tidak
                if(CalendarHelper.getAttendanceHours() > getLimit.toString()){
                    AlertHelper.runVoiceAndToast(voiceHelper, context, "Gagal absen karena telat masuk pagi")
                    return false
                }

                val data: Map<String, Any?> = mapOf(
                    "user_id" to id,
                    "status" to AttendanceStatus.DEFAULT_ATTENDANCE_STATUS,
                    "role" to role,
                    "created_at" to CalendarHelper.getAttendanceTimestamps(),
                    "updated_at" to CalendarHelper.getAttendanceTimestamps()
                )
                dbHelper.insertData("attendances", data)
            }

            attendance = getCurrentAttendance(id, dbHelper)

            if(attendance.moveToFirst()){
                val hours = CalendarHelper.convertToLocalTime(CalendarHelper.getAttendanceHours())
                val indexCheckinStarts = day.getColumnIndex("checkin_starts")
                val indexCheckinEnds = day.getColumnIndex("checkin_ends")
                val indexBreakStarts = day.getColumnIndex("break_starts")
                val indexBreakEnds = day.getColumnIndex("break_ends")
                val indexReturnStarts = day.getColumnIndex("return_starts")
                val indexReturnEnds = day.getColumnIndex("return_ends")
                val indexCheckoutStarts = day.getColumnIndex("checkout_starts")
                val indexCheckoutEnds = day.getColumnIndex("checkout_ends")

                val checkinStarts = CalendarHelper.convertToLocalTime(day.getString(indexCheckinStarts))
                val checkinEnds = CalendarHelper.convertToLocalTime(day.getString(indexCheckinEnds))
                val breakStarts = CalendarHelper.convertToLocalTime(day.getString(indexBreakStarts))
                val breakEnds = CalendarHelper.convertToLocalTime(day.getString(indexBreakEnds))
                val returnStarts = CalendarHelper.convertToLocalTime(day.getString(indexReturnStarts))
                val returnEnds = CalendarHelper.convertToLocalTime(day.getString(indexReturnEnds))
                val checkoutStarts = CalendarHelper.convertToLocalTime(day.getString(indexCheckoutStarts))
                val checkoutEnds = CalendarHelper.convertToLocalTime(day.getString(indexCheckoutEnds))

                val indexAttendanceId = attendance.getColumnIndex("id")
                val attendanceId = attendance.getString(indexAttendanceId)

                if (isBetween(checkinStarts, checkinEnds, hours)) {
                    // absen pagi antara jam 07.00 - 08.15 (toleransi 15 menit)
                    if(dbHelper.insertDetailAttendance(context, "detail_attendances", mapOf(
                            "attendance_id" to attendanceId,
                            "status" to AttendanceStatus.PRESENT,
                            "created_at" to CalendarHelper.getAttendanceTimestamps(),
                            "updated_at" to CalendarHelper.getAttendanceTimestamps()
                        ), voiceHelper)){
                        AlertHelper.runVoiceAndToast(voiceHelper, context, "$name Berhasil absen pagi")
                    }
                    return true
                } else if(isBetween(breakStarts, breakEnds, hours)){
                    // absen istirahat antara jam 11.00 - 12.00
                    if(dbHelper.insertDetailAttendance(context, "detail_attendances", mapOf(
                            "attendance_id" to attendanceId,
                            "status" to AttendanceStatus.BREAK,
                            "created_at" to CalendarHelper.getAttendanceTimestamps(),
                            "updated_at" to CalendarHelper.getAttendanceTimestamps()
                        ), voiceHelper)){
                        AlertHelper.runVoiceAndToast(voiceHelper, context, "$name Berhasil absen istirahat")
                    }
                    return true
                } else if(isBetween(returnStarts, returnEnds, hours)){
                    // absen kembali antara jam 12.30 - 13.00
                    if(dbHelper.insertDetailAttendance(context, "detail_attendances", mapOf(
                            "attendance_id" to attendanceId,
                            "status" to AttendanceStatus.RETURN_BREAK,
                            "created_at" to CalendarHelper.getAttendanceTimestamps(),
                            "updated_at" to CalendarHelper.getAttendanceTimestamps()
                        ), voiceHelper)){
                        AlertHelper.runVoiceAndToast(voiceHelper, context, "$name Berhasil absen kembali")
                    }
                    return true
                } else if(isBetween(checkoutStarts, checkoutEnds, hours)){
                    // absen pulang antara jam 16.00 - 20.00
                    if(dbHelper.insertDetailAttendance(context, "detail_attendances", mapOf(
                            "attendance_id" to attendanceId,
                            "status" to AttendanceStatus.RETURN,
                            "created_at" to CalendarHelper.getAttendanceTimestamps(),
                            "updated_at" to CalendarHelper.getAttendanceTimestamps()
                        ), voiceHelper)){
                        AlertHelper.runVoiceAndToast(voiceHelper, context, "$name Berhasil absen pulang")
                    }
                    return true
                } else {
                    AlertHelper.runVoiceAndToast(voiceHelper, context, "Jam absen tidak tersedia")
                }

                return false
            }
        } else{
            AlertHelper.runVoiceAndToast(voiceHelper, context, "Tidak ada jam absen hari ini")
        }

        return false
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun isBetween(start: LocalTime, end: LocalTime, hours: LocalTime): Boolean {
        return hours in start..end
    }

    @OptIn(ObsoleteCoroutinesApi::class)
    private fun startActivity(context: Context, rfid: String, name: String, id: String, role: String) {
        context.startActivity(
            Intent(context, MainActivity::class.java)
            .putExtra("rfid", rfid)
            .putExtra("name", name)
            .putExtra("id", id)
            .putExtra("role", role))
    }
}