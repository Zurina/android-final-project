package com.harbourspace.unsplash.model

import android.os.Parcelable
import com.google.android.material.internal.ParcelableSparseArray
import kotlinx.android.parcel.Parcelize

@Parcelize
data class LinksX(
    val html: String,
    val likes: String,
    val photos: String,
    val portfolio: String,
    val self: String
) : Parcelable