package com.andriukhov.mymovies.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.andriukhov.mymovies.converters.Converter
import com.andriukhov.mymovies.database.dao.GenreDao
import com.andriukhov.mymovies.database.dao.MovieDao
import com.andriukhov.mymovies.pojo.Favorite
import com.andriukhov.mymovies.pojo.Genre
import com.andriukhov.mymovies.pojo.Movie

@Database(entities = [Movie::class, Favorite::class, Genre::class], version = 7, exportSchema = false)
@TypeConverters(value = [Converter::class])
abstract class MovieDatabase : RoomDatabase() {

    abstract fun movieDao(): MovieDao
    abstract fun genreDao(): GenreDao

    companion object {

        @Volatile
        private var INSTANCE: MovieDatabase? = null

        fun getInstance(context: Context): MovieDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MovieDatabase::class.java,
                    "movie_database"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}