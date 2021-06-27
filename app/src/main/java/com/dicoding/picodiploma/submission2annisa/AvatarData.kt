package com.dicoding.picodiploma.submission2annisa

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class AvatarData(

    var photoavatar: String? =null,
    var username: String? =null,
    var nameavatar: String? =null,
    var location: String? =null,
    var company: String? =null,
    var follower: String? =null,
    var following: String? =null,
    var repository: String? =null,
) : Parcelable
