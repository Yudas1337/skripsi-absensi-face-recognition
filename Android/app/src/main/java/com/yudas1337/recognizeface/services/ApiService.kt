package com.yudas1337.recognizeface.services

import android.content.Context
import android.database.Cursor
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.yudas1337.recognizeface.constants.ConstShared
import com.yudas1337.recognizeface.constants.Role
import com.yudas1337.recognizeface.database.DBHelper
import com.yudas1337.recognizeface.database.DBManager
import com.yudas1337.recognizeface.database.SharedPref
import com.yudas1337.recognizeface.helpers.AlertHelper
import com.yudas1337.recognizeface.network.Value
import com.yudas1337.recognizeface.network.config.RetrofitBuilder
import com.yudas1337.recognizeface.screens.SyncActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import okhttp3.MediaType
import okhttp3.RequestBody
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class ApiService(private val context: Context) {

    private lateinit var initCall: Call<Value>

    fun getStudents(){
        val call = RetrofitBuilder.builder().getStudents()

        CoroutineScope(Dispatchers.Main).launch {
            try {
                val response = call.await()
                val responseData = response.result
                val dbHelper = DBHelper(context, null)
                val sharedPreferences = context.getSharedPreferences(ConstShared.fileName, Context.MODE_PRIVATE)

                response.total?.let {
                    SharedPref.putInt(sharedPreferences, ConstShared.TOTAL_STUDENTS, it)
                }

                response.md5?.let{
                    SharedPref.putString(sharedPreferences, ConstShared.MD5_STUDENTS, it)
                }

                withContext(Dispatchers.IO) {
                    DBManager(dbHelper, context, sharedPreferences).insertStudentsFromJson(responseData)
                }

            } catch (e: Exception) {
                Log.d("wajahnya", "gagal ${e.message}")
                dismissDialog()
            }
        }
    }

    fun getEmployees() {
        val call: Call<Value> = RetrofitBuilder.employeeBuilder().getEmployees()

        CoroutineScope(Dispatchers.Main).launch {
            try {
                val response = call.await()
                val responseData = response.result
                val dbHelper = DBHelper(context, null)
                val sharedPreferences = context.getSharedPreferences(ConstShared.fileName, Context.MODE_PRIVATE)

                response.total?.let {
                    SharedPref.putInt(sharedPreferences, ConstShared.TOTAL_EMPLOYEES, it)
                }

                response.md5?.let{
                    SharedPref.putString(sharedPreferences, ConstShared.MD5_EMPLOYEES, it)
                }

                withContext(Dispatchers.IO) {
                    DBManager(dbHelper, context, sharedPreferences).insertEmployeesFromJson(responseData)
                }

            } catch (e: Exception) {
                dismissDialog()
            }
        }
    }

    fun getSchedules(){
        val call: Call<Value> = RetrofitBuilder.builder().getSchedules()

        call.enqueue(object : Callback<Value> {
            override fun onResponse(call: Call<Value>, response: Response<Value>) {
                if (response.isSuccessful) {
                    val responseData = response.body()?.result
                    val dbHelper = DBHelper(context, null)

                    val sharedPreferences = context.getSharedPreferences(ConstShared.fileName, Context.MODE_PRIVATE)
                    response.body()?.total?.let {
                        SharedPref.putInt(sharedPreferences, ConstShared.TOTAL_SCHEDULES, it)
                    }

                    response.body()?.md5?.let{
                        SharedPref.putString(sharedPreferences, ConstShared.MD5_SCHEDULES, it)
                    }

                    DBManager(dbHelper, context, sharedPreferences).insertSchedulesFromJson(responseData)
                } else {
                    dismissDialog()
                    Log.d("connFailure", "Gagal jadwal")
                }
            }

            override fun onFailure(call: Call<Value>, t: Throwable) {
                dismissDialog()
                Log.d("connFailure", "Gagal jadwal ${t.message}")
            }
        })

    }

    fun getAttendanceLimit(){
        val call: Call<Value> = RetrofitBuilder.builder().getAttendanceLimit()

        call.enqueue(object : Callback<Value> {
            override fun onResponse(call: Call<Value>, response: Response<Value>) {
                if (response.isSuccessful) {
                    val responseData = response.body()?.result
                    val dbHelper = DBHelper(context, null)

                    val sharedPreferences = context.getSharedPreferences(ConstShared.fileName, Context.MODE_PRIVATE)
                    response.body()?.total?.let {
                        SharedPref.putInt(sharedPreferences, ConstShared.TOTAL_LIMIT, it)
                    }

                    response.body()?.md5?.let{
                        SharedPref.putString(sharedPreferences, ConstShared.MD5_LIMIT, it)
                    }

                    DBManager(dbHelper, context, sharedPreferences).insertAttendanceLimitFromJson(responseData)
                } else {
                    dismissDialog()
                    Log.d("connFailure", "Gagal limit absensi")
                }
            }

            override fun onFailure(call: Call<Value>, t: Throwable) {
                dismissDialog()
                Log.d("connFailure", "Gagal limit absensi ${t.message}")
            }
        })

    }

    fun syncAttendances(){
        val dbHelper = DBHelper(context, null)

        val employees = dbHelper.syncAttendances(Role.EMPLOYEE)
        val students = dbHelper.syncAttendances(Role.STUDENT)

        if(employees.moveToFirst()){
            convertAttendances(employees, Role.EMPLOYEE)
        }

        if(students.moveToFirst()){
            convertAttendances(students, Role.STUDENT)
        }
    }

    private fun convertAttendances(cursor: Cursor, role:String){
        val dataMap = mutableMapOf<String, JSONObject>()

        cursor.let {

            if(it.moveToFirst()){
                do{
                    val idxUserId = cursor.getColumnIndex("user_id")
                    val idxStatus = cursor.getColumnIndex("status")
                    val idxCreatedAt = cursor.getColumnIndex("created_at")
                    val idxUpdatedAt = cursor.getColumnIndex("updated_at")
                    val idxDetailStatus = cursor.getColumnIndex("detailStatus")
                    val idxDetailCreatedAt = cursor.getColumnIndex("detailCreatedAt")
                    val idxDetailUpdatedAt = cursor.getColumnIndex("detailUpdatedAt")

                    val userId = cursor.getString(idxUserId)
                    val status = cursor.getString(idxStatus)
                    val createdAt = cursor.getString(idxCreatedAt)
                    val updatedAt = cursor.getString(idxUpdatedAt)
                    val attendanceStatus = cursor.getString(idxDetailStatus)
                    val attendanceCreatedAt = cursor.getString(idxDetailCreatedAt)
                    val attendanceUpdatedAt = cursor.getString(idxDetailUpdatedAt)

                    val attendanceObject = JSONObject()
                    attendanceObject.put("status", attendanceStatus)
                    attendanceObject.put("created_at", attendanceCreatedAt)
                    attendanceObject.put("updated_at", attendanceUpdatedAt)

                    if (!dataMap.containsKey(userId)) {
                        val userObject = JSONObject()
                        userObject.put("user_id", userId)
                        userObject.put("status", status)
                        userObject.put("created_at", createdAt)
                        userObject.put("updated_at", updatedAt)

                        val attendanceArray = JSONArray()
                        attendanceArray.put(attendanceObject)

                        userObject.put("detail_attendances", attendanceArray)

                        dataMap[userId] = userObject
                    } else {
                        val existingUserObject = dataMap[userId]!!

                        existingUserObject.getJSONArray("detail_attendances").put(attendanceObject)

                        dataMap[userId] = existingUserObject
                    }
                }while(it.moveToNext())
            }
        }

        val jsonArray = JSONArray(dataMap.values)

        val jsonObject = JSONObject()
        jsonObject.put("data", jsonArray)

        uploadAttendances(jsonObject, role)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun uploadAttendances(jsonObject: JSONObject, role: String){

        val dbHelper = DBHelper(context, null)

        val body: RequestBody = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString())

        initCall = if(role == Role.EMPLOYEE){
            RetrofitBuilder.employeeBuilder().syncAttendances(body)
        } else{
            RetrofitBuilder.builder().syncAttendances(body)
        }

        initCall.enqueue(object : Callback<Value> {
            override fun onResponse(call: Call<Value>, response: Response<Value>) {
                if (response.isSuccessful) {

                    if(context is SyncActivity){
                        context.pDialog.dismissWithAnimation()
                        response.body()?.message?.let {
                            if( dbHelper.updateAttendances()> 0){
                                AlertHelper.successDialog(
                                    context,
                                    contentText = it
                                )
                            }
                        }
                    }

                } else {
                    if(context is SyncActivity){
                        dismissDialog()
                    }
                    Log.d("wajahnya", "Gagal sinkron presensi ${response.code()} ${response.message()} ${response.body()}")
                }
            }

            override fun onFailure(call: Call<Value>, t: Throwable) {
                if(context is SyncActivity){
                    dismissDialog()
                }
                Log.d("wajahnya", "Gagal sinkron presensi ${t.message}")
            }
        })

        DBHelper(context, null).deleteOldRecords()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    suspend fun <T> Call<T>.await(): T {
        return suspendCancellableCoroutine { continuation ->
            enqueue(object : Callback<T> {
                override fun onResponse(call: Call<T>, response: Response<T>) {
                    if (response.isSuccessful) {
                        continuation.resume(response.body()!!)
                    } else {
                        continuation.resumeWithException(Exception("Failed to execute call: ${response.code()}"))
                    }
                }

                override fun onFailure(call: Call<T>, t: Throwable) {
                    continuation.resumeWithException(t)
                }
            })

            continuation.invokeOnCancellation {
                cancel()
            }
        }
    }

    fun getEmployeeFaces(){
        val call: Call<Value> = RetrofitBuilder.employeeBuilder().getEmployeeFaces()

        CoroutineScope(Dispatchers.Main).launch {
            try {
                val response = call.await()
                val responseData = response.result
                val dbHelper = DBHelper(context, null)
                val sharedPreferences = context.getSharedPreferences(ConstShared.fileName, Context.MODE_PRIVATE)

                response.md5?.let{
                    SharedPref.putString(sharedPreferences, ConstShared.MD5_EMPLOYEE_FACES, it)
                }

                withContext(Dispatchers.IO) {
                    DBManager(dbHelper, context, sharedPreferences).insertEmployeeFacesFromJson(responseData)
                }

            } catch (e: Exception) {
                dismissDialog()
            }
        }
    }

    fun getStudentFaces(){
        val call: Call<Value> = RetrofitBuilder.builder().getStudentFaces()

        CoroutineScope(Dispatchers.Main).launch {
            try {
                val response = call.await()
                val responseData = response.result
                val dbHelper = DBHelper(context, null)
                val sharedPreferences = context.getSharedPreferences(ConstShared.fileName, Context.MODE_PRIVATE)

                response.md5?.let{
                    SharedPref.putString(sharedPreferences, ConstShared.MD5_STUDENT_FACES, it)
                }

                withContext(Dispatchers.IO) {
                    DBManager(dbHelper, context, sharedPreferences).insertStudentFacesFromJson(responseData)
                }

            } catch (e: Exception) {
                dismissDialog()
            }
        }
    }

    private fun dismissDialog(){
        if(context is SyncActivity){
            context.pDialog.dismissWithAnimation()
            AlertHelper.errorDialog(
                context,
                contentText = "Koneksi Gagal! Silahkan Ulangi dan Periksa Koneksi Internet Anda"
            )
        }
    }
}