package com.yudas1337.recognizeface.database

import android.database.sqlite.SQLiteDatabase

class Table {

    companion object{
        const val roles = "roles"
        const val schools = "schools"
        const val users = "users"
        const val students = "students"
        const val attendances = "attendances"
        const val detail_attendances = "detail_attendances"

        fun setupTables(db: SQLiteDatabase){
            val createAttendances = ("CREATE TABLE IF NOT EXISTS $roles (" +
                    "id CHAR(36) PRIMARY KEY, " +
                    "role_name TEXT, " +
                    "created_at TEXT, " +
                    "updated_at TEXT" +
                    ")")

            val createSchools = ("CREATE TABLE IF NOT EXISTS $schools (" +
                    "id CHAR(36) PRIMARY KEY, " +
                    "name TEXT, " +
                    "email TEXT, " +
                    "contact TEXT, " +
                    "address TEXT, " +
                    "created_at TEXT, " +
                    "updated_at TEXT" +
                    ")")

            val createUsers = ("CREATE TABLE IF NOT EXISTS $users (" +
                    "id CHAR(36) PRIMARY KEY, " +
                    "rfid TEXT, " +
                    "name TEXT, " +
                    "photo TEXT, " +
                    "status TEXT CHECK(status IN ('aktif', 'selesai', 'keluar')), " +
                    "gender TEXT CHECK(status IN ('pria', 'wanita')), " +
                    "role_id CHAR(36) REFERENCES roles(id), " +
                    "school_id CHAR(36) NULL REFERENCES schools(id)," +
                    "created_at TEXT, " +
                    "updated_at TEXT" +
                    ")")

            db.execSQL(createAttendances)
            db.execSQL(createSchools)
            db.execSQL(createUsers)
        }
    }

}