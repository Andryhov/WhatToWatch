package com.andriukhov.mymovies.api

class ApiHelper(private val apiService: ApiService) {
    suspend fun getPopularityMovies(language: String, page: Int) =
        apiService.getPopularityMovies(language, page)

    suspend fun getTopRatedMovies(language: String, page: Int) =
        apiService.getTopRatedMovies(language, page)

    suspend fun getTrailers(id: Int, language: String) = apiService.getTrailers(id, language)

    suspend fun getReviews(id: Int, language: String) = apiService.getReviews(id, language)
}