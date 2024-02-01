package com.yudas1337.recognizeface.database

import android.database.sqlite.SQLiteDatabase

class Table {

    companion object{
        const val students = "students"
        const val schedules = "schedules"
        const val employees = "employees"

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
                    "name TEXT, " +
                    "email TEXT, " +
                    "id_number TEXT, " +
                    "position TEXT, " +
                    "photo TEXT, " +
                    "gender TEXT, " +
                    "wages TEXT, " +
                    "rfid TEXT, " +
                    "address TEXT, " +
                    "date_of_birth TEXT" +
                    ")")
        }
    }

}