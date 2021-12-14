package com.example.mymovies.repository

import androidx.annotation.WorkerThread
import com.example.mymovies.dao.MovieDao
import com.example.mymovies.db.Favorite
import com.example.mymovies.db.Movie
import kotlinx.coroutines.flow.Flow

class MoviesRepository(private val movieDao: MovieDao) {

    val getAllMovies = movieDao.getAllMoviesSortByDesc()

    fun getMovieById(id: Int): Flow<Movie> = movieDao.getMovieById(id)

    val getAllFavouriteMovies = movieDao.getAllFavouriteMovies()

    fun getFavouriteMovieById(id: Int): Flow<Favorite> = movieDao.getFavouriteMovieById(id)

    suspend fun insertMovie(movie: Movie) {
        return movieDao.insertMovie(movie)
    }

    @WorkerThread
    suspend fun deleteMovie(movie: Movie) {
        movieDao.deleteMovie(movie)
    }

    @WorkerThread
    suspend fun insertFavouriteMovie(favorite: Favorite) {
        movieDao.insertFavouriteMovie(favorite)
    }

    @WorkerThread
    suspend fun deleteFavouriteMovie(favorite: Favorite) {
        movieDao.deleteFavouriteMovie(favorite)
    }

    @WorkerThread
    suspend fun deleteAllMovies() {
        movieDao.deleteAllMovies()
    }
}