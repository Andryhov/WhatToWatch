package com.andriukhov.mymovies

import android.app.Application
import com.andriukhov.mymovies.api.ApiFactory
import com.andriukhov.mymovies.database.MovieDatabase
import com.andriukhov.mymovies.repository.MoviesRepository

class MoviesApplication : Application() {
    private val moviesDatabase by lazy { MovieDatabase.getInstance(this) }
    val moviesRepository by lazy {
        MoviesRepository(moviesDatabase.movieDao(), ApiFactory.getApiService())
    }
}