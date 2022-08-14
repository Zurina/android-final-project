package com.harbourspace.unsplash.data.cb

import com.harbourspace.unsplash.model.UnsplashItem

interface UnsplashResult {

    fun onDataFetchedSuccess(images: List<UnsplashItem>)

    fun onPhotoByIdFetchedSuccess(image: UnsplashItem)

    fun onDataFetchedFailed()
}