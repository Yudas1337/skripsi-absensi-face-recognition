package com.yudas1337.recognizeface.constants

import android.os.Environment
import java.io.File

object FaceFolder {

    // shared pref for presensi_faces directory name
    const val DIR_FACES_NAME = "presensi_faces"

    // shared pref for employees directory name
    const val EMPLOYEE_DIR_FACES_NAME = "employees"

    // shared pref for students directory name
    const val STUDENTS_DIR_FACES_NAME = "students"

    // shared pref for cropped face
    const val CROPPED_FACE = "cropped_face.png"

    // shared pref for profile directory
    const val PROFILE_FACES_NAME = "profiles"

    // get download directory
    private val downloadDir: File = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)

    // get DIR_FACES_NAME
    val facesDir: File = File(downloadDir, DIR_FACES_NAME)

    // get PROFILE_FACES_NAME
    val profileDir: File = File(downloadDir, PROFILE_FACES_NAME)
}