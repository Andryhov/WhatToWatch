package com.andriukhov.mymovies.repository

import com.andriukhov.mymovies.api.ApiHelper

class RetrofitRepository(private val apiHelper: ApiHelper) {
    suspend fun getPopularityMovies(language: String, page: Int) =
        apiHelper.getPopularityMovies(language, page)

    suspend fun getTopRatedMovies(language: String, page: Int) =
        apiHelper.getTopRatedMovies(language, page)

    suspend fun getTrailers(id: Int, language: String) = apiHelper.getTrailers(id, language)

    suspend fun getReviews(id: Int, language: String) = apiHelper.getReviews(id, language)
}