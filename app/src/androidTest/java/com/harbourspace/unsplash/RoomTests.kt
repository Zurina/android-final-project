package com.harbourspace.unsplash

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.harbourspace.unsplash.db.AppDatabase
import com.harbourspace.unsplash.db.ImageUrlDAO
import com.harbourspace.unsplash.model.ImageUrl
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.After

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Before
import java.io.IOException

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class RoomTests {
    private lateinit var imageUrlDao: ImageUrlDAO
    private lateinit var db: AppDatabase

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, AppDatabase::class.java).build()
        imageUrlDao = db.imageUrlDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun insertImageUrlAndRetrieveItByAuthorName() {
        val imageUrl = ImageUrl("123", "https://url.com", "j.k rowling", "description")
        imageUrlDao.insertAll(imageUrl)
        val imageUrlFromDb = imageUrlDao.getPhotosBySearchPattern("j.k rowling")
        MatcherAssert.assertThat(
            imageUrlFromDb[0].authorName,
            CoreMatchers.equalTo("j.k rowling")
        )
    }

    @Test
    @Throws(Exception::class)
    fun insertImageUrlAndRetrieveItByDescription() {
        val imageUrl = ImageUrl("123", "https://url.com", "j.k rowling", "random description")
        imageUrlDao.insertAll(imageUrl)
        val imageUrlFromDb = imageUrlDao.getPhotosBySearchPattern("random description")
        MatcherAssert.assertThat(
            imageUrlFromDb[0].authorName,
            CoreMatchers.equalTo("j.k rowling")
        )
    }
}