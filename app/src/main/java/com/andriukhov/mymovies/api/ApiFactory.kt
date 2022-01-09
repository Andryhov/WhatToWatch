package com.andriukhov.mymovies.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiFactory {
    private const val BASE_URL = "https://api.themoviedb.org/3/"
    const val BASE_URL_YOUTUBE = "https://www.youtube.com/watch?v="
    const val BASE_IMG_URL = "https://image.tmdb.org/t/p"
    const val SMALL_POSTER_SIZE = "/w185"
    const val BIG_POSTER_SIZE = "/w780"

    private val retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(BASE_URL)
        .build()

    fun getApiService(): ApiService {
        return retrofit.create(ApiService::class.java)
    }
}