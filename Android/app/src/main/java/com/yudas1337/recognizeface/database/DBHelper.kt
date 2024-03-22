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

    fun getStudents(name: String): Cursor? {

        val db = this.readableDatabase

        return db.rawQuery("SELECT * FROM students WHERE name LIKE ? OR school LIKE ?", arrayOf("%$name%", "%$name%"))
    }

    fun getEmployees(name: String): Cursor? {

        val db = this.readableDatabase

        return db.rawQuery("SELECT * FROM employees WHERE name LIKE ?", arrayOf("%$name%"))
    }

    fun truncateTables(tableNames: Array<String>) {
        val db = this.writableDatabase

        tableNames.forEach { tableName ->
            db.execSQL("DELETE FROM $tableName")
        }
    }

    fun countUnsyncAttendances(): Int {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT COUNT(*) FROM detail_attendances WHERE is_uploaded = 0", null)
        var total = 0

        cursor.use {
            if (it.moveToFirst()) {
                total = it.getInt(0)
            }
        }

        return total
    }

    fun updateAttendances(): Int {
        val db = this.writableDatabase

        val values = ContentValues()
        values.put("is_uploaded", 1)

        val rowsAffected = db.update("detail_attendances", values, null, null)
        db.close()

        return rowsAffected
    }

    fun syncAttendances(role: String): Cursor {
        val db = this.readableDatabase

        val query = """
    SELECT
        attendances.user_id,
        attendances.status,
        attendances.created_at,
        attendances.updated_at,
        detail_attendances.status AS detailStatus,
        detail_attendances.created_at AS detailCreatedAt,
        detail_attendances.updated_at AS detailUpdatedAt
    FROM
        attendances
    JOIN
        detail_attendances ON attendances.id = detail_attendances.attendance_id
    WHERE
        detail_attendances.is_uploaded = 0
    AND 
        attendances.role = ?
""".trimIndent()

        return db.rawQuery(query, arrayOf(role))
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
        WHERE students.name LIKE ?
        GROUP BY 
            students.name
        ORDER BY 
            students.name ASC;
    """.trimIndent()

        return db.rawQuery(query, arrayOf("%$name%"))
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
        WHERE employees.name LIKE ?
        GROUP BY 
            employees.name
        ORDER BY 
            employees.name ASC;
    """.trimIndent()

        return db.rawQuery(query, arrayOf("%$name%"))
    }

    companion object{

        private const val DATABASE_NAME = "db_attendance"

        private const val DATABASE_VERSION = 1

    }
}