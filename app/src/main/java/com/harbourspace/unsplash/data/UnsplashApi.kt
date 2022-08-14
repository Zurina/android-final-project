package com.harbourspace.unsplash.data

import com.harbourspace.unsplash.model.UnsplashItem
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path

private const val AUTHORIZATION_CLIENT_ID = "Client-ID"
private const val ACCESS_KEY = "ngak5Lv2ZsDvYfnAJjyMP0mnV23pWs5hcvOBXceV3Wc"

interface UnsplashAPIClient {

    @Headers("Authorization: $AUTHORIZATION_CLIENT_ID $ACCESS_KEY")
    @GET("photos/random?count=1&query=quote")
    fun fetchPhotos() : Call<List<UnsplashItem>>

    @Headers("Authorization: $AUTHORIZATION_CLIENT_ID $ACCESS_KEY")
    @GET("photos/{id}")
    fun fetchPhotoById(@Path("id") id : String) : Call<UnsplashItem>
}