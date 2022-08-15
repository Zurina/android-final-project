package com.harbourspace.unsplash.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Likes(
    val total: Int,
) : Parcelable