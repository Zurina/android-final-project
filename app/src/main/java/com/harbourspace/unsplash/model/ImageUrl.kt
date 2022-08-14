package com.harbourspace.unsplash.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class ImageUrl(
    @PrimaryKey val uid: UUID,
    @ColumnInfo(name = "url") val url: String
)