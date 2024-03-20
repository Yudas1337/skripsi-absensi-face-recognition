package com.yudas1337.recognizeface.database

import android.database.sqlite.SQLiteDatabase

class Table {

    companion object{
        const val students = "students"
        const val schedules = "schedules"
        const val employees = "employees"
        const val attendance_rule = "attendance_rule"
        const val attendances = "attendances"
        const val detail_attendances = "detail_attendances"

        fun setupTables(db: SQLiteDatabase){
            db.execSQL("CREATE TABLE IF NOT EXISTS $students (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "name TEXT, " +
                    "email TEXT, " +
                    "photo TEXT, " +
                    "national_student_number TEXT, " +
                    "classroom INTEGER, " +
                    "school TEXT, " +
                    "rfid TEXT, " +
                    "gender TEXT, " +
                    "address TEXT, " +
                    "phone_number TEXT, " +
                    "created_at TEXT, " +
                    "updated_at TEXT" +
                    ")")

            db.execSQL("CREATE TABLE IF NOT EXISTS $schedules (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "day TEXT, " +
                    "checkin_starts TEXT, " +
                    "checkin_ends TEXT, " +
                    "break_starts TEXT, " +
                    "break_ends TEXT, " +
                    "return_starts TEXT, " +
                    "return_ends TEXT, " +
                    "checkout_starts TEXT, " +
                    "checkout_ends TEXT" +
                    ")")

            db.execSQL("CREATE TABLE IF NOT EXISTS $employees (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "uuid TEXT, " +
                    "name TEXT, " +
                    "email TEXT, " +
                    "national_identity_number TEXT, " +
                    "phone_number TEXT, " +
                    "position TEXT, " +
                    "photo TEXT, " +
                    "gender TEXT, " +
                    "salary INTEGER, " +
                    "rfid TEXT, " +
                    "address TEXT, " +
                    "date_of_birth TEXT, " +
                    "created_at TEXT, " +
                    "updated_at TEXT" +
                    ")")

            db.execSQL("CREATE TABLE IF NOT EXISTS $attendance_rule (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "minute TEXT " +
                    ")")

            db.execSQL("CREATE TABLE IF NOT EXISTS $attendances (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "user_id TEXT, " +
                    "status TEXT, " +
                    "role TEXT, " +
                    "created_at TEXT, " +
                    "updated_at TEXT" +
                    ")")

            db.execSQL("CREATE TABLE IF NOT EXISTS $detail_attendances (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "attendance_id TEXT, " +
                    "is_uploaded TEXT," +
                    "status TEXT, " +
                    "created_at TEXT, " +
                    "updated_at TEXT" +
                    ")")
        }
    }

}