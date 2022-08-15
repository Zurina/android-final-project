package com.harbourspace.unsplash.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Statistics(
    val views: Views,
    val downloads: Downloads,
    val likes: Likes,
) : Parcelable