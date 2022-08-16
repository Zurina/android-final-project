package com.harbourspace.unsplash

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.harbourspace.unsplash.data.ImageUrlDAO
import com.harbourspace.unsplash.data.UnsplashApiProvider
import com.harbourspace.unsplash.data.cb.UnsplashResult
import com.harbourspace.unsplash.model.ImageUrl
import com.harbourspace.unsplash.model.UnsplashItem

private const val TAG = "UnsplashViewModel"

class UnsplashViewModel: ViewModel(), UnsplashResult {

    private val _unsplashItems = MutableLiveData<List<UnsplashItem>>()
    val unsplashItems: LiveData<List<UnsplashItem>> = _unsplashItems

    private val _unsplashItem = MutableLiveData<UnsplashItem>()
    val unsplashItem: LiveData<UnsplashItem> = _unsplashItem

    private val _error = MutableLiveData<Boolean>()
    val error: LiveData<Boolean> = _error

    private val provider by lazy {
        UnsplashApiProvider()
    }

    fun fetchImages() {
        provider.fetchImages(this)
    }

    fun fetchImageById(id : String) {
        provider.fetchImageById(id, this)
    }

    override fun onDataFetchedSuccess(images: List<UnsplashItem>) {
        Log.d(TAG, "onDataFetchedSuccess | Received ${images.size} images")
        _unsplashItems.value = images
    }

    override fun onPhotoByIdFetchedSuccess(image: UnsplashItem) {
        Log.d(TAG, "onPhotoByIdFetchedSuccess | Received ${image.urls.regular} images")
        _unsplashItem.value = image
    }

    override fun onDataFetchedFailed() {
        Log.d(TAG, "onDataFetchedFailed | Unable to retrieve images")
        _error.value = true
    }
}