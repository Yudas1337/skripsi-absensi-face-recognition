package com.yudas1337.recognizeface.database

import android.database.sqlite.SQLiteDatabase

class Table {

    companion object{
        const val students = "students"
        const val attendances = "attendances"

        fun setupTables(db: SQLiteDatabase){

            val createStudents = ("CREATE TABLE IF NOT EXISTS $students (" +
                    "id INT(11) PRIMARY KEY, " +
                    "name TEXT, " +
                    "email TEXT, " +
                    "photo TEXT, " +
                    "national_student_number TEXT, " +
                    "classroom INT(11), " +
                    "school TEXT, " +
                    "rfid TEXT, " +
                    "gender TEXT CHECK(status IN ('Laki-laki', 'Perempuan')), " +
                    "address TEXT, " +
                    "phone_number TEXT, " +
                    "created_at TEXT, " +
                    "updated_at TEXT" +
                    ")")

            db.execSQL(createStudents)
        }
    }

}