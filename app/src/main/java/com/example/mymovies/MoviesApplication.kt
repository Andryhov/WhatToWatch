package com.example.mymovies

import android.app.Application
import com.example.mymovies.db.MovieDatabase
import com.example.mymovies.repository.MoviesRepository

class MoviesApplication: Application() {
    private val moviesDatabase by lazy { MovieDatabase.getInstance(this) }
    val moviesRepository by lazy { MoviesRepository(moviesDatabase.movieDao()) }
}