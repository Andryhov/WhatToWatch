package com.andriukhov.mymovies

import android.app.Application
import com.andriukhov.mymovies.api.ApiFactory
import com.andriukhov.mymovies.api.ApiHelper
import com.andriukhov.mymovies.data.MovieDatabase
import com.andriukhov.mymovies.repository.MoviesRepository

class MoviesApplication : Application() {
    private val moviesDatabase by lazy { MovieDatabase.getInstance(this) }
    val moviesRepository by lazy {
        MoviesRepository(
            moviesDatabase.movieDao(),
            ApiHelper(ApiFactory.getInstance()!!.getApiService())
        )
    }
}