package com.dicoding.picodiploma.consummerapp.db

import android.database.Cursor
import com.dicoding.picodiploma.consummerapp.Favorite.FavoriteData
import com.dicoding.picodiploma.consummerapp.db.DatabaseContract.AvatarFavColumn.Companion.AVATAR
import com.dicoding.picodiploma.consummerapp.db.DatabaseContract.AvatarFavColumn.Companion.COMPANY
import com.dicoding.picodiploma.consummerapp.db.DatabaseContract.AvatarFavColumn.Companion.ID
import com.dicoding.picodiploma.consummerapp.db.DatabaseContract.AvatarFavColumn.Companion.LOCATION
import com.dicoding.picodiploma.consummerapp.db.DatabaseContract.AvatarFavColumn.Companion.NAME
import com.dicoding.picodiploma.consummerapp.db.DatabaseContract.AvatarFavColumn.Companion.REPOSITORY
import com.dicoding.picodiploma.consummerapp.db.DatabaseContract.AvatarFavColumn.Companion.USERNAME


object MapFavHelper {

    fun listCursor(cursorfav: Cursor?): ArrayList<FavoriteData>{
        val dataFav = ArrayList<FavoriteData>()
        cursorfav?.apply {
            while (moveToNext()) {
                val id = getInt(getColumnIndexOrThrow(ID))
                val avatar = getString(getColumnIndexOrThrow(AVATAR))
                val username = getString(getColumnIndexOrThrow(USERNAME))
                val name = getString(getColumnIndexOrThrow(NAME))
                val location = getString(getColumnIndexOrThrow(LOCATION))
                val company = getString(getColumnIndexOrThrow(COMPANY))
                val repository = getInt(getColumnIndexOrThrow(REPOSITORY))
                dataFav.add(
                    FavoriteData(
                        id,
                        avatar,
                        username,
                        name,
                        location,
                        company,
                        repository
                    )
                )
            }
        }
        return dataFav
    }
    fun objectCursor(favCursor: Cursor?): FavoriteData {
        var datafav = FavoriteData()
        favCursor?.apply {
            moveToFirst()
            val id = getInt(getColumnIndexOrThrow(ID))
            val avatar = getString(getColumnIndexOrThrow(AVATAR))
            val username = getString(getColumnIndexOrThrow(USERNAME))
            val name = getString(getColumnIndexOrThrow(NAME))
            val location = getString(getColumnIndexOrThrow(LOCATION))
            val company = getString(getColumnIndexOrThrow(COMPANY))
            val repository = getInt(getColumnIndexOrThrow(REPOSITORY))
            datafav=
                FavoriteData(
                    id,
                    username,
                    name,
                    avatar,
                    company,
                    location,
                    repository
                    )
        }
        return datafav
    }

}