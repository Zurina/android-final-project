package com.harbourspace.unsplash.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.harbourspace.unsplash.model.ImageUrl

@Dao
interface ImageUrlDAO {
    @Query("SELECT * FROM imageurl")
    fun getAll(): List<ImageUrl>

    @Insert
    fun insertAll(vararg imageUrls: ImageUrl)
}