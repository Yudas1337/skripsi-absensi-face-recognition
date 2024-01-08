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


    fun insertData(tableName: String, data: Map<String, String>){

        val values = ContentValues()
        val db = this.writableDatabase

        for ((columnName, value) in data) {
            values.put(columnName, value)
        }


        db.insert(tableName, null, values)

//        db.close()

    }

    fun getName(): Cursor? {

        val db = this.readableDatabase

//        return db.rawQuery("SELECT * FROM " + TABLE_NAME, null)

        return db.rawQuery("SELECT * FROM roles", null)
    }

    companion object{

        private const val DATABASE_NAME = "db_attendance"

        private const val DATABASE_VERSION = 1
    }
}