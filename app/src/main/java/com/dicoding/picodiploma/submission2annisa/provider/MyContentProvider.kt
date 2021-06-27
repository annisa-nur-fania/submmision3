package com.dicoding.picodiploma.submission2annisa.provider

import android.content.ContentProvider
import android.content.ContentValues
import android.content.Context
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import com.dicoding.picodiploma.submission2annisa.Favorite.FavoriteHelper
import com.dicoding.picodiploma.submission2annisa.db.DatabaseContract

class MyContentProvider : ContentProvider() {

    companion object {
        private const val AVATAR_FAV = 1
        private const val AVATAR_FAV_ID = 2
        private lateinit var favoriteHelper: FavoriteHelper
        private val sUriMatcher = UriMatcher(UriMatcher.NO_MATCH)

        init {
            sUriMatcher.addURI(
                DatabaseContract.AUTHORITY,
                DatabaseContract.AvatarFavColumn.TABLE_NAME,
                AVATAR_FAV)
            sUriMatcher.addURI(
                DatabaseContract.AUTHORITY,
                "${DatabaseContract.AvatarFavColumn.TABLE_NAME}/#",
                AVATAR_FAV_ID)
        }
    }

    override fun onCreate(): Boolean {

        favoriteHelper= FavoriteHelper.getInstance(context as Context)
        favoriteHelper.open()
        return true

    }

    override fun query(uri: Uri, projection: Array<out String>?, selection: String?, selectionArgs: Array<out String>?, sortOrder: String?): Cursor? {
        return when (sUriMatcher.match(uri)) {
            AVATAR_FAV -> favoriteHelper.getDataUser()
            AVATAR_FAV_ID -> favoriteHelper.getDataId(uri.lastPathSegment.toString())
            else -> null
        }
    }

    override fun getType(uri: Uri): String? {
        return null
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        val added: Long = when (AVATAR_FAV) {
            sUriMatcher.match(uri) -> favoriteHelper.insertData(values)
            else -> 0
        }
        context?.contentResolver?.notifyChange(DatabaseContract.AvatarFavColumn.CONTENT_URI, null)

        return Uri.parse("${DatabaseContract.AvatarFavColumn.CONTENT_URI}/$added")
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {

        val deleted: Int = when (AVATAR_FAV_ID) {
            sUriMatcher.match(uri) -> favoriteHelper.deleteDataName(uri.lastPathSegment.toString())
            else -> 0
        }
        context?.contentResolver?.notifyChange(DatabaseContract.AvatarFavColumn.CONTENT_URI, null)
        return deleted
    }
    override fun update(
        uri: Uri, values: ContentValues?, selection: String?,
        selectionArgs: Array<String>?
    ): Int {
        TODO("Implement this to handle requests to delete one or more rows")
    }
}