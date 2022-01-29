package com.andriukhov.mymovies.api

import com.andriukhov.mymovies.pojo.Movie
import com.andriukhov.mymovies.pojo.Review
import com.andriukhov.mymovies.pojo.Trailer
import com.andriukhov.mymovies.response.ActorsResponse
import com.andriukhov.mymovies.response.DataResponse
import com.andriukhov.mymovies.response.GenreResponse
import com.andriukhov.mymovies.response.ImagesResponse
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

    @GET("movie/{id}/images?api_key=$API_KEY&include_image_language=null")
    suspend fun getImages(
        @Path("id") id: Int,
        @Query("language") language: String
    ): ImagesResponse

    @GET("movie/{id}/credits?api_key=$API_KEY")
    suspend fun getActorsByMovieId(
        @Path("id") id: Int,
        @Query("language") language: String
    ): ActorsResponse
}