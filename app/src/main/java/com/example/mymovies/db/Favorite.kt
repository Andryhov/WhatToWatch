package com.example.mymovies.db

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "favourite_movies")
class Favorite(
    @PrimaryKey(autoGenerate = true)
    override val uniqueId: Int,
    override val id: Int,
    override val voteCount: Int,
    override val title: String,
    override val originalTitle: String,
    override val overview: String,
    override val posterPath: String,
    override val bigPosterPath: String,
    override val backDropPath: String,
    override val voteAverage: Double,
    override val releaseDate: String
)
    : Movie(
    uniqueId,
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
{
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
    constructor(movie: Movie) : this(
        uniqueId = 0,
        movie.id,
        movie.voteCount,
        movie.title,
        movie.originalTitle,
        movie.overview,
        movie.posterPath,
        movie.bigPosterPath,
        movie.backDropPath,
        movie.voteAverage,
        movie.releaseDate
    )
}