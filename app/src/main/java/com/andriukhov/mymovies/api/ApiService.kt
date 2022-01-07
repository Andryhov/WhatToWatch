package com.andriukhov.mymovies.api

import com.andriukhov.mymovies.data.Genre
import com.andriukhov.mymovies.data.Movie
import com.andriukhov.mymovies.data.Review
import com.andriukhov.mymovies.data.Trailer
import com.andriukhov.mymovies.pojo.DataResponse
import com.andriukhov.mymovies.pojo.GenreResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    companion object {
        private const val API_KEY = "a8d15abd143f64da04630e5c490dc134"
        private const val PARAMS_MIN_VOTE_COUNT = "vote_count.gte"
        private const val SORT_BY_POPULARITY = "popularity.desc"
        private const val SORT_BY_TOP_RATED = "vote_average.desc"
        private const val MIN_VOTE_COUNT_VALUE = "1000"
    }

    @GET("discover/movie?api_key=$API_KEY&sort_by=$SORT_BY_POPULARITY&$PARAMS_MIN_VOTE_COUNT=$MIN_VOTE_COUNT_VALUE")
    suspend fun getPopularityMovies(
        @Query("language") language: String,
        @Query("page") page: Int
    ): DataResponse<Movie>

    @GET("discover/movie?api_key=$API_KEY&sort_by=$SORT_BY_TOP_RATED&$PARAMS_MIN_VOTE_COUNT=$MIN_VOTE_COUNT_VALUE")
    suspend fun getTopRatedMovies(
        @Query("language") language: String,
        @Query("page") page: Int
    ): DataResponse<Movie>

    @GET("movie/{id}/videos?api_key=$API_KEY")
    suspend fun getTrailers(
        @Path("id") id: Int,
        @Query("language") language: String
    ): DataResponse<Trailer>

    @GET("movie/{id}/reviews?api_key=$API_KEY")
    suspend fun getReviews(
        @Path("id") id: Int,
        @Query("language") language: String
    ): DataResponse<Review>

    @GET("genre/movie/list?api_key=$API_KEY")
    suspend fun getGenres(
        @Query("language") language: String
    ): GenreResponse
}