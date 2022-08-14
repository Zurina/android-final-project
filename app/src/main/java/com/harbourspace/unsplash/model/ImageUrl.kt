package com.harbourspace.unsplash.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class ImageUrl(
    @PrimaryKey val id: String,
    @ColumnInfo(name = "url") val url: String,
    @ColumnInfo(name = "authorName") val authorName: String?,
    @ColumnInfo(name = "description") val description: String?
)
