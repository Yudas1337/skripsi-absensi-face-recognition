package com.yudas1337.recognizeface.services

import android.content.Context
import android.os.Handler
import com.yudas1337.recognizeface.database.DBHelper
import com.yudas1337.recognizeface.helpers.FaceHelper

class SyncService(private val context: Context, private val dbHelper: DBHelper) {

    fun syncUsers() {
        dbHelper.truncateTables(arrayOf("students", "employees"))
        ApiService(context).getStudents()

        Handler().postDelayed({
            ApiService(context).getEmployees()
        }, 2000L)

    }

    fun syncSchedules(){
        dbHelper.truncateTables(arrayOf("schedules", "attendance_rule"))
        ApiService(context).getSchedules()

        Handler().postDelayed({
            ApiService(context).getAttendanceLimit()
        }, 2000L)
    }

    fun syncAttendances(){
        dbHelper.truncateTables(arrayOf("attendances", "detail_attendances"))
    }

    fun syncAttendanceFaces(){

        FaceHelper.initAttendanceFaceDirectory()

        ApiService(context).getStudentFaces()

        Handler().postDelayed({
            ApiService(context).getEmployeeFaces()
        }, 2000L)

    }
}