package com.dicoding.picodiploma.consummerapp.Favorite

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class FavoriteData(
    var id: Int = 0,
    var avatar: String? = null,
    var username: String? = null,
    var name: String? = null,
    var location: String? = null,
    var company: String? = null,
    var repository: Int? = 0,
    var follower: Int? = 0,
    var following: Int? = 0,
): Parcelable
