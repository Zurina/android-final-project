package com.harbourspace.unsplash.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.harbourspace.unsplash.model.ImageUrl

@Database(entities = [ImageUrl::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun imageUrlDao(): ImageUrlDAO
}