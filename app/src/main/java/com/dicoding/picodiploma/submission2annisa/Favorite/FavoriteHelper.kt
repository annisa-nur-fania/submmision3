package com.dicoding.picodiploma.submission2annisa.Favorite

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.dicoding.picodiploma.submission2annisa.db.DatabaseContract.AvatarFavColumn.Companion.ID
import com.dicoding.picodiploma.submission2annisa.db.DatabaseContract.AvatarFavColumn.Companion.NAME
import com.dicoding.picodiploma.submission2annisa.db.DatabaseContract.AvatarFavColumn.Companion.TABLE_NAME
import com.dicoding.picodiploma.submission2annisa.db.DatabaseContract.AvatarFavColumn.Companion.USERNAME
import com.dicoding.picodiploma.submission2annisa.db.DatabaseHelper
import java.sql.SQLException

class FavoriteHelper (context: Context) {
    private var dataBaseHelper: DatabaseHelper = DatabaseHelper(context)

    companion object {

        private const val GET_TABLE = TABLE_NAME
        private var INSTANCE: FavoriteHelper?=null

        fun getInstance(context: Context): FavoriteHelper =
                INSTANCE ?: synchronized(this) {
                    INSTANCE ?: FavoriteHelper(context)
                }
        private lateinit var database:SQLiteDatabase
    }
    init {
        dataBaseHelper = DatabaseHelper(context)
    }
    @Throws(SQLException::class)
    fun open() {
        database = dataBaseHelper.writableDatabase
    }
    fun close() {
        dataBaseHelper.close()
        if (database.isOpen)
            database.close()
    }
    fun getDataUser(): Cursor {
        return database.query(
                GET_TABLE,
                null,
                null,
                null,
                null,
                null,
                "$USERNAME ASC",
                null

        )
    }
    fun getDataName(id: String): Cursor {
        return database.query(
            GET_TABLE,
            null,
            "$NAME = ?",
            arrayOf(id),
            null,
            null,
            null,
            null
        )
    }
    fun getDataId(id: String): Cursor {
        return database.query(
                GET_TABLE,
                null,
                "$ID = ?",
                arrayOf(id),
                null,
                null,
                null,
                null
        )
    }
    fun insertData(values: ContentValues?): Long {
        return database.insert(GET_TABLE, null, values)
    }

    fun deleteDataId(id: String): Int {
        return database.delete(GET_TABLE, "$ID = '$id'", null)
    }
    fun deleteDataName(id: String): Int {
        return database.delete(GET_TABLE, "$NAME = '$id'", null)
    }

}