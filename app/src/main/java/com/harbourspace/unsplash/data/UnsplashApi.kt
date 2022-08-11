package com.harbourspace.unsplash.data

import com.harbourspace.unsplash.model.UnsplashItem
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers

private const val AUTHORIZATION_CLIENT_ID = "Client-ID"
private const val ACCESS_KEY = ""

interface UnsplashAPIClient {

    @Headers("Authorization: $AUTHORIZATION_CLIENT_ID $ACCESS_KEY")
    @GET("photos/random?count=1&query=quote")
    fun fetchPhotos() : Call<List<UnsplashItem>>
}