package com.yudas1337.recognizeface.services

import android.content.Context
import android.os.Handler
import com.yudas1337.recognizeface.constants.FaceFolder
import com.yudas1337.recognizeface.database.DBHelper
import com.yudas1337.recognizeface.helpers.FaceHelper

class SyncService(private val context: Context, private val dbHelper: DBHelper) {

    fun syncEmployees(){
        dbHelper.truncateTables(arrayOf("employees"))
        FaceHelper.initProfileDirectory(FaceFolder.EMPLOYEE_DIR_FACES_NAME)
        ApiService(context).getEmployees()
    }

    fun syncStudents(){
        dbHelper.truncateTables(arrayOf("students"))
        FaceHelper.initProfileDirectory(FaceFolder.STUDENTS_DIR_FACES_NAME)
        ApiService(context).getStudents()
    }

    fun syncSchedules(){
        dbHelper.truncateTables(arrayOf("schedules", "attendance_rule"))
        ApiService(context).getSchedules()

        Handler().postDelayed({
            ApiService(context).getAttendanceLimit()
        }, 2000L)
    }

    fun syncAttendances(){
        ApiService(context).syncAttendances()
    }

    fun syncStudentFaces(){
        FaceHelper.initAttendanceFaceDirectory(FaceFolder.STUDENTS_DIR_FACES_NAME)
        ApiService(context).getStudentFaces()
    }

    fun syncEmployeeFaces(){
        FaceHelper.initAttendanceFaceDirectory(FaceFolder.EMPLOYEE_DIR_FACES_NAME)
        ApiService(context).getEmployeeFaces()
    }
}