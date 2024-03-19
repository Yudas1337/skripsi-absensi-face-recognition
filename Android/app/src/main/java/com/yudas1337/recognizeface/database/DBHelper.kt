package com.yudas1337.recognizeface.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.os.Build
import androidx.annotation.RequiresApi

class DBHelper(context: Context, factory: SQLiteDatabase.CursorFactory?) :
    SQLiteOpenHelper(context, DATABASE_NAME, factory, DATABASE_VERSION) {
    override fun onCreate(db: SQLiteDatabase) {
        Table.setupTables(db)
    }

    override fun onUpgrade(db: SQLiteDatabase, p1: Int, p2: Int) {
        db.execSQL("DROP TABLE IF EXISTS " + Table.students)
        onCreate(db)
    }

    fun insertData(tableName: String, data: Map<String, Any?>){

        val values = ContentValues()
        val db = this.writableDatabase

        for ((columnName, value) in data) {
            when (value) {
                is String -> values.put(columnName, value)
                is Int -> values.put(columnName, value)
                else -> throw IllegalArgumentException("Unsupported data type for column $columnName")
            }
        }

        db.insertWithOnConflict(tableName, null, values, SQLiteDatabase.CONFLICT_REPLACE)
    }

    fun countTableData(table: String): Int {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT COUNT(*) FROM $table", null)
        var total = 0

        cursor.use {
            if (it.moveToFirst()) {
                total = it.getInt(0)
            }
        }

        return total
    }

    fun getScheduleDay(day: String): Cursor {
        val db = this.readableDatabase
        return db.rawQuery("SELECT * FROM schedules WHERE day = ? ", arrayOf(day))
    }

    fun getAttendanceLimit(): Long? {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM attendance_rule", null);

        var minute: Long? = null

        cursor?.use {
            if (it.moveToFirst()) {
                val minuteIndex = it.getColumnIndex("minute")
                minute = cursor.getString(minuteIndex).toLong()
            }
        }

        return minute
    }

    fun insertDetailAttendance(tableName: String, data: Map<String, Any?>): Boolean{
        val db = this.readableDatabase
        val attendanceId = data["attendance_id"].toString()
        val status = data["status"].toString()

        val query = db.rawQuery("SELECT * FROM detail_attendances WHERE attendance_id = ? AND status = ?", arrayOf(attendanceId, status))

        return if(query.moveToFirst()){
            false
        } else{
            insertData(tableName, data)
            true
        }
    }

    fun getTodayAttendance(userId: String, date: String): Cursor {
        val db = this.readableDatabase
        return db.rawQuery("SELECT * FROM attendances WHERE user_id = ? AND date(created_at) = ?", arrayOf(userId, date))
    }

    fun findUsersByRfid(table: String, rfid: String): Cursor? {
        val db = this.readableDatabase
        return db.rawQuery("SELECT * FROM $table WHERE rfid = ?", arrayOf(rfid))
    }

    fun getStudents(): Cursor? {

        val db = this.readableDatabase

        return db.rawQuery("SELECT * FROM students", null)
    }

    fun getEmployees(): Cursor? {

        val db = this.readableDatabase

        return db.rawQuery("SELECT * FROM employees", null)
    }

    fun truncateTables(tableNames: Array<String>) {
        val db = this.writableDatabase

        tableNames.forEach { tableName ->
            db.execSQL("DELETE FROM $tableName")
        }
    }

    fun syncAttendances(): Cursor {
        val db = this.readableDatabase

        return db.rawQuery("SELECT\n" +
                "    attendances.user_id,\n" +
                "    attendances.status,\n" +
                "    attendances.created_at,\n" +
                "    attendances.updated_at,\n" +
                "    json_group_array(\n" +
                "        json_object(\n" +
                "            'status', detail_attendances.status,\n" +
                "            'created_at', detail_attendances.created_at,\n" +
                "            'updated_at', detail_attendances.updated_at\n" +
                "        )\n" +
                "    ) AS detail_attendances\n" +
                "FROM\n" +
                "    attendances\n" +
                "LEFT JOIN\n" +
                "    detail_attendances ON attendances.id = detail_attendances.attendance_id\n" +
                "GROUP BY\n" +
                "    attendances.id", null)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun fetchStudentAttendances(name: String): Cursor {
        val db = this.readableDatabase

        val query = """
        SELECT 
            students.name,
            students.school,
            attendances.status,
            GROUP_CONCAT(CASE WHEN detail_attendances.status = 'present' THEN detail_attendances.created_at ELSE NULL END) AS present_hour,
            GROUP_CONCAT(CASE WHEN detail_attendances.status = 'break' THEN detail_attendances.created_at ELSE NULL END) AS break_hour,
            GROUP_CONCAT(CASE WHEN detail_attendances.status = 'return_break' THEN detail_attendances.created_at ELSE NULL END) AS return_break_hour,
            GROUP_CONCAT(CASE WHEN detail_attendances.status = 'return' THEN detail_attendances.created_at ELSE NULL END) AS return_hour
        FROM 
            students
        LEFT JOIN 
            attendances ON students.id = attendances.user_id AND DATE(attendances.created_at) = DATE("now")
        LEFT JOIN 
            detail_attendances ON attendances.id = detail_attendances.attendance_id
        GROUP BY 
            students.name
        ORDER BY 
            students.name ASC;
    """.trimIndent()

        return db.rawQuery(query, null)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun fetchEmployeeAttendances(name: String): Cursor {
        val db = this.readableDatabase

        val query = """
        SELECT 
            employees.name,
            attendances.status,
            GROUP_CONCAT(CASE WHEN detail_attendances.status = 'present' THEN detail_attendances.created_at ELSE NULL END) AS present_hour,
            GROUP_CONCAT(CASE WHEN detail_attendances.status = 'break' THEN detail_attendances.created_at ELSE NULL END) AS break_hour,
            GROUP_CONCAT(CASE WHEN detail_attendances.status = 'return_break' THEN detail_attendances.created_at ELSE NULL END) AS return_break_hour,
            GROUP_CONCAT(CASE WHEN detail_attendances.status = 'return' THEN detail_attendances.created_at ELSE NULL END) AS return_hour
        FROM 
            employees
        LEFT JOIN 
            attendances ON employees.uuid = attendances.user_id AND DATE(attendances.created_at) = DATE("now")
        LEFT JOIN 
            detail_attendances ON attendances.id = detail_attendances.attendance_id
        GROUP BY 
            employees.name
        ORDER BY 
            employees.name ASC;
    """.trimIndent()

        return db.rawQuery(query, null)
    }

    companion object{

        private const val DATABASE_NAME = "db_attendance"

        private const val DATABASE_VERSION = 1

    }
}