package com.yudas1337.recognizeface.database

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.yudas1337.recognizeface.constants.FaceFolder
import com.yudas1337.recognizeface.constants.URL
import com.yudas1337.recognizeface.helpers.AlertHelper
import com.yudas1337.recognizeface.helpers.PackageHelper
import com.yudas1337.recognizeface.network.Result
import com.yudas1337.recognizeface.recognize.BitmapUtils
import com.yudas1337.recognizeface.screens.SyncActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import java.io.File

class DBManager(private val dbHelper: DBHelper,
                private val context: Context,
                private val sharedPreferences: SharedPreferences?) {

    private var processedDataCount: Int = 0

    suspend fun insertStudentFacesFromJson(jsonData: List<Result>?){
        try {
            if (jsonData != null) {
                processedDataCount = 0

                val jobs = mutableListOf<Deferred<Boolean>>()

                for (i in jsonData.indices) {
                    val faces = jsonData[i].faces
                    val rfid = jsonData[i].rfid
                    val studentDir = File(FaceFolder.facesDir, "${FaceFolder.STUDENTS_DIR_FACES_NAME}/$rfid")

                    if (!studentDir.exists() && !studentDir.isDirectory) studentDir.mkdir()

                    val job = CoroutineScope(Dispatchers.IO).async {
                        var allFacesSaved = true

                        faces?.forEach {
                            val bitmap = BitmapUtils.downloadImage(it.photo!!, URL.STUDENT_FACES_DIR)
                            if (bitmap != null) {
                                val save = BitmapUtils.saveBitmap(bitmap, File(studentDir, "${PackageHelper.generateUUID()}.png"))
                                if (!save) {
                                    allFacesSaved = false
                                }
                            } else {
                                allFacesSaved = false
                            }
                        }

                        allFacesSaved
                    }

                    jobs.add(job)
                }

                val results = jobs.awaitAll()

                val allFacesSaved = results.all { it }
                val ctx = context as SyncActivity

                if (!allFacesSaved) {
                    ctx.runOnUiThread {
                        ctx.pDialog.dismissWithAnimation()
                        AlertHelper.errorDialog(
                            ctx,
                            contentText = "Beberapa Wajah Gagal Disimpan! Silahkan Ulangi dan Periksa Koneksi Internet Anda"
                        )
                    }
                } else {
                    ctx.runOnUiThread {
                        ctx.pDialog.dismissWithAnimation()
                        AlertHelper.successDialog(
                            ctx,
                            contentText = "Sinkronisasi Wajah Siswa Berhasil"
                        )
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    suspend fun insertEmployeeFacesFromJson(jsonData: List<Result>?){
        try {
            if (jsonData != null) {
                processedDataCount = 0

                val jobs = mutableListOf<Deferred<Boolean>>()

                for (i in jsonData.indices) {
                    val faces = jsonData[i].faces
                    val rfid = jsonData[i].rfid
                    val employeeDir = File(FaceFolder.facesDir, "${FaceFolder.EMPLOYEE_DIR_FACES_NAME}/$rfid")

                    if (!employeeDir.exists() && !employeeDir.isDirectory) employeeDir.mkdir()

                    val job = CoroutineScope(Dispatchers.IO).async {
                        var allFacesSaved = true

                        faces?.forEach {
                            val bitmap = BitmapUtils.downloadImage(it.photo!!, URL.EMPLOYEE_FACES_DIR)
                            if (bitmap != null) {
                                val save = BitmapUtils.saveBitmap(bitmap, File(employeeDir, "${PackageHelper.generateUUID()}.png"))
                                if (!save) {
                                    allFacesSaved = false
                                }
                            } else {
                                allFacesSaved = false
                                
                            }
                        }

                        allFacesSaved
                    }

                    jobs.add(job)
                }

                val results = jobs.awaitAll()

                val allFacesSaved = results.all { it }
                val ctx = context as SyncActivity

                if (!allFacesSaved) {
                    ctx.runOnUiThread {
                        ctx.pDialog.dismissWithAnimation()
                        AlertHelper.errorDialog(
                            ctx,
                            contentText = "Beberapa Wajah Gagal Disimpan! Silahkan Ulangi dan Periksa Koneksi Internet Anda"
                        )
                    }
                } else {
                    ctx.runOnUiThread {
                        ctx.pDialog.dismissWithAnimation()
                        AlertHelper.successDialog(
                            ctx,
                            contentText = "Sinkronisasi Wajah Pegawai Berhasil"
                        )
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun insertStudentsFromJson(jsonData: List<Result>?) {
        try {
            if (jsonData != null) {
                val jobs = mutableListOf<Deferred<Boolean>>()
                val studentDir = File(FaceFolder.profileDir, FaceFolder.STUDENTS_DIR_FACES_NAME)

                for (i in jsonData.indices) {
                    val rfid = jsonData[i].rfid
                    val data: Map<String, Any?> = mapOf(
                        "id" to  jsonData[i].id,
                        "name" to jsonData[i].name,
                        "email" to jsonData[i].email,
                        "photo" to "${rfid}.png",
                        "national_student_number" to jsonData[i].national_student_number,
                        "classroom" to jsonData[i].classroom,
                        "school" to jsonData[i].school,
                        "rfid" to rfid,
                        "gender" to jsonData[i].gender,
                        "address" to jsonData[i].address,
                        "phone_number" to jsonData[i].phone_number,
                        "created_at" to jsonData[i].created_at,
                        "updated_at" to jsonData[i].updated_at,
                    )

                    dbHelper.insertData(Table.students, data)

                    if (!studentDir.exists() && !studentDir.isDirectory) studentDir.mkdir()

                    val job = CoroutineScope(Dispatchers.IO).async {
                        var allFacesSaved = true
                        val bitmap = BitmapUtils.downloadImage(jsonData[i].photo!!, URL.STUDENT_STORAGE)
                        if (bitmap != null) {
                            val save = BitmapUtils.saveBitmap(bitmap, File(studentDir, "${rfid}.png"))
                            if (!save) {
                                allFacesSaved = false
                            }
                        } else {
                            allFacesSaved = false
                            
                         }

                        allFacesSaved
                    }

                    jobs.add(job)
                }

                val results = jobs.awaitAll()

                val allFacesSaved = results.all { it }
                val ctx = context as SyncActivity

                if (!allFacesSaved) {
                    ctx.runOnUiThread {
                        ctx.pDialog.dismissWithAnimation()
                        AlertHelper.errorDialog(
                            ctx,
                            contentText = "Beberapa Data Gagal Disimpan! Silahkan Ulangi dan Periksa Koneksi Internet Anda"
                        )
                    }
                } else {
                    ctx.runOnUiThread {
                        ctx.pDialog.dismissWithAnimation()
                        AlertHelper.successDialog(
                            ctx,
                            contentText = "Sinkronisasi Siswa Magang Berhasil"
                        )
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun insertEmployeesFromJson(jsonData: List<Result>?){

        try {
            if (jsonData != null) {

                val jobs = mutableListOf<Deferred<Boolean>>()
                val employeeDir = File(FaceFolder.profileDir, FaceFolder.EMPLOYEE_DIR_FACES_NAME)

                for (i in jsonData.indices) {
                    val rfid = jsonData[i].rfid
                    val data: Map<String, Any?> = mapOf(
                        "uuid" to jsonData[i].id,
                        "name" to jsonData[i].name,
                        "email" to jsonData[i].email,
                        "national_identity_number" to jsonData[i].national_identity_number,
                        "phone_number" to jsonData[i].phone_number,
                        "position" to jsonData[i].position,
                        "photo" to "${rfid}.png",
                        "gender" to jsonData[i].gender,
                        "salary" to jsonData[i].salary,
                        "rfid" to jsonData[i].rfid,
                        "address" to jsonData[i].address,
                        "date_of_birth" to jsonData[i].date_of_birth,
                    )
                    dbHelper.insertData(Table.employees, data)

                    if (!employeeDir.exists() && !employeeDir.isDirectory) employeeDir.mkdir()

                    val job = CoroutineScope(Dispatchers.IO).async {
                        var allFacesSaved = true
                        val bitmap = BitmapUtils.downloadImage(jsonData[i].photo!!, URL.EMPLOYEE_FACES_DIR)
                        if (bitmap != null) {
                            val save = BitmapUtils.saveBitmap(bitmap, File(employeeDir, "${rfid}.png"))
                            if (!save) {
                                allFacesSaved = false
                            }
                        } else {
                            allFacesSaved = false
                            
                        }

                        allFacesSaved
                    }

                    jobs.add(job)
                }

                val results = jobs.awaitAll()

                val allFacesSaved = results.all { it }
                val ctx = context as SyncActivity

                if (!allFacesSaved) {
                    ctx.runOnUiThread {
                        ctx.pDialog.dismissWithAnimation()
                        AlertHelper.errorDialog(
                            ctx,
                            contentText = "Beberapa Data Gagal Disimpan! Silahkan Ulangi dan Periksa Koneksi Internet Anda"
                        )
                    }
                } else {
                    ctx.runOnUiThread {
                        ctx.pDialog.dismissWithAnimation()
                        AlertHelper.successDialog(
                            ctx,
                            contentText = "Sinkronisasi Pegawai Berhasil"
                        )
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun insertSchedulesFromJson(jsonData: List<Result>?){
        try {
            if (jsonData != null) {
                for (i in jsonData.indices) {
                    val data: Map<String, Any?> = mapOf(
                        "day" to jsonData[i].day,
                        "checkin_starts" to jsonData[i].checkin_starts,
                        "checkin_ends" to jsonData[i].checkin_ends,
                        "break_starts" to jsonData[i].break_starts,
                        "break_ends" to jsonData[i].break_ends,
                        "return_starts" to jsonData[i].return_starts,
                        "return_ends" to jsonData[i].return_ends,
                        "checkout_starts" to jsonData[i].checkout_starts,
                        "checkout_ends" to jsonData[i].checkout_ends
                    )
                    dbHelper.insertData(Table.schedules, data)
                }
            }
        }
        catch (e: Exception){
            e.printStackTrace()
        }
    }

    fun insertAttendanceLimitFromJson(jsonData: List<Result>?){
        try {
            if (jsonData != null) {
                for (i in jsonData.indices) {
                    val data: Map<String, Any?> = mapOf(
                        "minute" to jsonData[i].minute,
                    )
                    dbHelper.insertData(Table.attendance_rule, data)

                    if(context is SyncActivity){
                        context.pDialog.dismissWithAnimation()
                        AlertHelper.successDialog(context, contentText = "Sinkronisasi Jadwal Berhasil")
                    }

                }
            }
        }
        catch (e: Exception){
            e.printStackTrace()
        }
    }
}