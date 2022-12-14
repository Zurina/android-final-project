package com.harbourspace.unsplash.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.harbourspace.unsplash.model.ImageUrl

@Dao
interface ImageUrlDAO {
    @Query("SELECT * FROM imageurl")
    fun getAll(): List<ImageUrl>

    @Query("SELECT * FROM imageurl where authorName LIKE :pattern OR description LIKE :pattern")
    fun getPhotosBySearchPattern(pattern : String): List<ImageUrl>

    @Insert
    fun insertAll(vararg imageUrls: ImageUrl)
}