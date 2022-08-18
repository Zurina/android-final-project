package com.harbourspace.unsplash.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.harbourspace.unsplash.model.ImageUrl

@Database(entities = [ImageUrl::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun imageUrlDao(): ImageUrlDAO
}