package com.dicoding.picodiploma.consummerapp.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.dicoding.picodiploma.consummerapp.db.DatabaseContract.AvatarFavColumn.Companion.TABLE_NAME


internal class DatabaseHelper  (context: Context):
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "user_Database"
        private const val DATABASE_VERSION = 1
        private const val SQL_CREATE_TABLE_NOTE = "CREATE TABLE $TABLE_NAME"+
                "(${DatabaseContract.AvatarFavColumn.ID} INTEGER PRIMARY KEY AUTOINCREMENT," +
                "${DatabaseContract.AvatarFavColumn.AVATAR} TEXT NOT NULL," +
                "${DatabaseContract.AvatarFavColumn.USERNAME} TEXT NOT NULL," +
                "${DatabaseContract.AvatarFavColumn.NAME} TEXT NOT NULL," +
                "${DatabaseContract.AvatarFavColumn.LOCATION} TEXT NOT NULL," +
                "${DatabaseContract.AvatarFavColumn.COMPANY} TEXT NOT NULL," +
                "${DatabaseContract.AvatarFavColumn.REPOSITORY} TEXT NOT NULL)"
    }
    override fun onCreate(datab: SQLiteDatabase?) {
        datab?.execSQL(SQL_CREATE_TABLE_NOTE)
    }
    override fun onUpgrade(datab: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        datab?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(datab)
    }
}