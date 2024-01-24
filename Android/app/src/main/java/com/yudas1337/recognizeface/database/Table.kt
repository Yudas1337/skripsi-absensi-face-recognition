package com.yudas1337.recognizeface.database

import android.database.sqlite.SQLiteDatabase

class Table {

    companion object{
        const val students = "students"

        fun setupTables(db: SQLiteDatabase){

            val createStudents = (
                    "CREATE TABLE IF NOT EXISTS $students (" +
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
                            ")"
                    )

            db.execSQL(createStudents)
        }
    }

}