package com.yudas1337.recognizeface.services

import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.yudas1337.recognizeface.constants.AttendanceStatus
import com.yudas1337.recognizeface.constants.Role
import com.yudas1337.recognizeface.database.DBHelper
import com.yudas1337.recognizeface.helpers.CalendarHelper
import com.yudas1337.recognizeface.helpers.PackageHelper
import com.yudas1337.recognizeface.screens.MainActivity
import kotlinx.coroutines.ObsoleteCoroutinesApi
import java.time.LocalTime

class ScanService(private val context: Context, private val dbHelper: DBHelper) {

    @RequiresApi(Build.VERSION_CODES.O)
    fun handleScan(rfid: String) {
        val students = dbHelper.findUsersByRfid("students", rfid)
        val employees = dbHelper.findUsersByRfid("employees", rfid)

        if (students != null && students.moveToFirst()) {
            val indexName = students.getColumnIndex("name")
            val name = students.getString(indexName)
            val indexId = students.getColumnIndex("id")
            val id = students.getString(indexId)

            if(handleAttendances(id, Role.SISWA)){
                startActivity(context, rfid, name)
                students.close()
                dbHelper.close()
            }
        } else if(employees != null && employees.moveToFirst()) {
            val indexName = employees.getColumnIndex("name")
            val name = employees.getString(indexName)
            val indexId = employees.getColumnIndex("uuid")
            val id = employees.getString(indexId)

            if(handleAttendances(id, Role.PEGAWAI)){
                startActivity(context, rfid, name)

                employees.close()
                dbHelper.close()
            }

        } else{
            Toast.makeText(context, "Kartu tidak terdaftar", Toast.LENGTH_SHORT).show()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getCurrentAttendance(id: String, dbHelper: DBHelper): Cursor {
        return dbHelper.getTodayAttendance(id, CalendarHelper.getDate())
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun handleAttendances(id: String, role: String): Boolean{
        val day = dbHelper.getScheduleDay(CalendarHelper.getToday())

        if(day.moveToFirst()){

            val getLimit = dbHelper.getAttendanceLimit()?.let { CalendarHelper.addLimitAttendance(day, it) }

            var attendance = getCurrentAttendance(id, dbHelper)

            // cek apakah belum absen masuk hari ini di pagi hari
            if(!attendance.moveToFirst()){
                // cek apakah dia telat atau tidak
                if(CalendarHelper.getAttendanceHours() > getLimit.toString()){
                    Toast.makeText(context, "Gagal absen karena telat masuk pagi", Toast.LENGTH_SHORT).show()
                    return false
                }
                val data: Map<String, Any?> = mapOf(
                    "user_id" to id,
                    "status" to AttendanceStatus.DEFAULT_ATTENDANCE_STATUS,
                    "role" to role,
                    "created_at" to PackageHelper.timestamp,
                    "updated_at" to PackageHelper.timestamp
                )
                dbHelper.insertData("attendances", data)
            }

            attendance = getCurrentAttendance(id, dbHelper)

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

            if (isBetween(checkinStarts, checkinEnds, hours)) {
                Log.d("absennya", "pagi Absen berada di rentang antara 07:00:00 dan 08:00:00")
            } else if(isBetween(breakStarts, breakEnds, hours)){
                Log.d("absennya", "istirahat Absen berada di rentang antara 11:00:00 dan 12:00:00")
            } else if(isBetween(returnStarts, returnEnds, hours)){
                Log.d("absennya", "istirahat Absen berada di rentang antara 12:30:00 dan 13:00:00")
            } else if(isBetween(checkoutStarts, checkoutEnds, hours)){
                Log.d("absennya", "istirahat Absen berada di rentang antara 16:00:00 dan 20:00:00")
            } else {
                Log.d("absennya", "Tidak tersedia jam absen")
                Toast.makeText(context, "Jam absen tidak tersedia", Toast.LENGTH_SHORT).show()
            }
        } else{
            // cek jika pada hari itu tidak ada jam absen
            Toast.makeText(context, "Tidak ada jam absen hari ini", Toast.LENGTH_SHORT).show()
        }

        return false
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun isBetween(start: LocalTime, end: LocalTime, hours: LocalTime): Boolean {
        return hours in start..end
    }

    @OptIn(ObsoleteCoroutinesApi::class)
    private fun startActivity(context: Context, rfid: String, name: String) {
        context.startActivity(
            Intent(context, MainActivity::class.java)
            .putExtra("rfid", rfid)
            .putExtra("name", name))
    }
}