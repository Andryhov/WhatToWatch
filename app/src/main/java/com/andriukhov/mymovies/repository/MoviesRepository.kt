package com.andriukhov.mymovies.repository

import androidx.annotation.WorkerThread
import com.andriukhov.mymovies.api.ApiHelper
import com.andriukhov.mymovies.dao.GenreDao
import com.andriukhov.mymovies.dao.MovieDao
import com.andriukhov.mymovies.data.Favorite
import com.andriukhov.mymovies.data.Genre
import com.andriukhov.mymovies.data.Movie
import kotlinx.coroutines.flow.Flow

class MoviesRepository(
    private val genreDao: GenreDao,
    private val movieDao: MovieDao,
    private val apiHelper: ApiHelper
) {

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

    suspend fun insertGenre(genre: Genre) {
        return genreDao.insertGenre(genre)
    }


    suspend fun getPopularityMovies(language: String, page: Int) =
        apiHelper.getPopularityMovies(language, page)

    suspend fun getTopRatedMovies(language: String, page: Int) =
        apiHelper.getTopRatedMovies(language, page)

    suspend fun getTrailers(id: Int, language: String) = apiHelper.getTrailers(id, language)

    suspend fun getReviews(id: Int, language: String) = apiHelper.getReviews(id, language)

    suspend fun loadGenres(language: String) = apiHelper.getGenres(language)
}