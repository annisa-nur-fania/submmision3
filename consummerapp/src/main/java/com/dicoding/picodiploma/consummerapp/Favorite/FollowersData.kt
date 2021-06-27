package com.dicoding.picodiploma.consummerapp.Favorite

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class FollowersData(
    var login: String? =null,
    var avatar: String? =null,
    var location : String? =null
): Parcelable
