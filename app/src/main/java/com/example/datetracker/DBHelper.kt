package com.example.datetracker

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(context: Context, factory: SQLiteDatabase.CursorFactory?):
    SQLiteOpenHelper(context, DATABASE_NAME, factory, DATABASE_VERSION) {
        override fun onCreate(db: SQLiteDatabase) {
           val query = ("CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ("
                    + ID_COL + " INTEGER PRIMARY KEY, " +
                    FIRST_DATE + " TEXT, " +
                    SECOND_DATE + " TEXT" +
                    ")")
            db.execSQL(query)
        }

        override fun onUpgrade(db: SQLiteDatabase, p1: Int, p2: Int) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME)
            onCreate(db)
        }

        fun addDates(firstDate: String, secondDate: String) {
            val values = ContentValues()
            values.put(FIRST_DATE, firstDate)
            values.put(SECOND_DATE, secondDate)
            val db = this.writableDatabase
            db.insert(TABLE_NAME, null, values)
            db.close()
        }

        fun getDates(): Cursor? {
            val db = this.readableDatabase
            return db.rawQuery("SELECT * FROM " + TABLE_NAME, null)
        }

        fun editDatesById(id: Int, first: String, second: String) {
            val db = this.readableDatabase
            val values = ContentValues()
            values.put(FIRST_DATE, first)
            values.put(SECOND_DATE, second)
            val idstr = "id = $id"
            db.update(TABLE_NAME, values, idstr, null)
        }

        fun deleteDatesById(id: Int): Int {
            val db = this.readableDatabase
            return db.delete(TABLE_NAME, "$ID_COL = $id", null)
        }

        fun removeAllDates() {
            val db = this.readableDatabase
            db.delete(TABLE_NAME, null, null)
        }

        companion object {
            private val DATABASE_NAME = "db1"
            private val DATABASE_VERSION = 1
            val TABLE_NAME = "daterange_table1"
            val ID_COL = "id1"
            val FIRST_DATE = "firstdate1"
            val SECOND_DATE = "seconddate1"
        }
}