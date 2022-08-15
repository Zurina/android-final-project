package com.harbourspace.unsplash.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Downloads(
    val total: Int,
) : Parcelable