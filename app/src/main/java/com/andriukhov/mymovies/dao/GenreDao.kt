package com.andriukhov.mymovies.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.andriukhov.mymovies.data.Genre
import kotlinx.coroutines.flow.Flow

@Dao
interface GenreDao {

    @Query("SELECT * FROM genre WHERE id == :id")
    fun getGenreById(id: Int): Flow<Genre>

    @Query("DELETE FROM genre")
    suspend fun removeAllGenres()

    @Insert
    suspend fun insertGenre(genre: Genre)
}