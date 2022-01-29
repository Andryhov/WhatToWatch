package com.andriukhov.mymovies.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.andriukhov.mymovies.pojo.Favorite
import com.andriukhov.mymovies.pojo.Movie
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao {

    @Query("SELECT * FROM movies ORDER BY voteAverage DESC")
    fun getAllMoviesSortByDesc(): Flow<List<Movie>>

    @Query("SELECT * FROM movies WHERE id == :idMovie")
    fun getMovieById(idMovie: Int): Flow<Movie>

    @Query("SELECT * FROM favourite_movies")
    fun getAllFavouriteMovies(): Flow<List<Favorite>>

    @Query("SELECT * FROM favourite_movies WHERE id == :idMovie")
    fun getFavouriteMovieById(idMovie: Int): Flow<Favorite>

    @Insert
    suspend fun insertMovie(movie: Movie)

    @Delete
    suspend fun deleteMovie(movie: Movie)

    @Insert
    suspend fun insertFavouriteMovie(favorite: Favorite)

    @Delete
    suspend fun deleteFavouriteMovie(favorite: Favorite)

    @Query("DELETE FROM movies")
    suspend fun deleteAllMovies()
}