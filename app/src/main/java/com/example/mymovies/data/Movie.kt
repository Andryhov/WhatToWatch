package com.example.mymovies.data

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "movies")
open class Movie(
    @PrimaryKey(autoGenerate = true)
    open val uniqueId: Int,
    open val id: Int,
    open val voteCount: Int,
    open val title: String,
    open val originalTitle: String,
    open val overview: String,
    open val posterPath: String,
    open val bigPosterPath: String,
    open val backDropPath: String,
    open val voteAverage: Double,
    open val releaseDate: String
) {
    @Ignore
    constructor(
        id: Int,
        voteCount: Int,
        title: String,
        originalTitle: String,
        overview: String,
        posterPath: String,
        bigPosterPath: String,
        backDropPath: String,
        voteAverage: Double,
        releaseDate: String
    ) : this(
        uniqueId = 0,
        id,
        voteCount,
        title,
        originalTitle,
        overview,
        posterPath,
        bigPosterPath,
        backDropPath,
        voteAverage,
        releaseDate
    )

    @Ignore
    constructor(favorite: Favorite) : this(
        uniqueId = 0,
        favorite.id,
        favorite.voteCount,
        favorite.title,
        favorite.originalTitle,
        favorite.overview,
        favorite.posterPath,
        favorite.bigPosterPath,
        favorite.backDropPath,
        favorite.voteAverage,
        favorite.releaseDate
    )
}