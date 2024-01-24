package com.yudas1337.recognizeface.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(context: Context, factory: SQLiteDatabase.CursorFactory?) :
    SQLiteOpenHelper(context, DATABASE_NAME, factory, DATABASE_VERSION) {
    override fun onCreate(db: SQLiteDatabase) {
        Table.setupTables(db)
    }

    override fun onUpgrade(db: SQLiteDatabase, p1: Int, p2: Int) {
        onCreate(db)
    }


    fun insertData(tableName: String, data: Map<String, Any>){

        val values = ContentValues()
        val db = this.writableDatabase

        for ((columnName, value) in data) {
            when (value) {
                is String -> values.put(columnName, value)
                is Int -> values.put(columnName, value)
                else -> throw IllegalArgumentException("Unsupported data type for column $columnName")
            }
        }

        db.insert(tableName, null, values)
    }

    fun getStudents(): Cursor? {

        val db = this.readableDatabase

//        return db.rawQuery("SELECT * FROM " + TABLE_NAME, null)

        return db.rawQuery("SELECT * FROM students", null)
    }

    companion object{

        private const val DATABASE_NAME = "db_attendance"

        private const val DATABASE_VERSION = 1
    }
}