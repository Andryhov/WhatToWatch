package com.andriukhov.mymovies.domain.pojo

import androidx.room.*
import com.andriukhov.mymovies.api.ApiFactory
import com.andriukhov.mymovies.converters.Converter
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Entity(tableName = "movies")
@TypeConverters(value = [Converter::class])
open class Movie(
    @PrimaryKey(autoGenerate = true)
    open val uniqueId: Int,
    @SerializedName("id")
    @Expose
    open val id: Int,
    @SerializedName("vote_count")
    @Expose
    open val voteCount: Int,
    @SerializedName("title")
    @Expose
    open val title: String,
    @SerializedName("original_title")
    @Expose
    open val originalTitle: String,
    @SerializedName("overview")
    @Expose
    open val overview: String,
    @SerializedName("poster_path")
    @Expose
    open val posterPath: String,
    @SerializedName("backdrop_path")
    @Expose
    open val backDropPath: String,
    @SerializedName("vote_average")
    @Expose
    open val voteAverage: Double,
    @SerializedName("release_date")
    @Expose
    open val releaseDate: String,
    @SerializedName("genre_ids")
    @Expose
    open val genreIds: List<Int>
) {
    @Ignore
    constructor(
        id: Int,
        voteCount: Int,
        title: String,
        originalTitle: String,
        overview: String,
        posterPath: String,
        backDropPath: String,
        voteAverage: Double,
        releaseDate: String,
        genreIds: List<Int>
    ) : this(
        uniqueId = 0,
        id,
        voteCount,
        title,
        originalTitle,
        overview,
        posterPath,
        backDropPath,
        voteAverage,
        releaseDate,
        genreIds
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
        favorite.backDropPath,
        favorite.voteAverage,
        favorite.releaseDate,
        favorite.genreIds
    )

    fun getFullSmallPosterPath(): String =
        ApiFactory.BASE_IMG_URL + ApiFactory.SMALL_POSTER_SIZE + posterPath

    fun getFullBigPosterPath(): String =
        ApiFactory.BASE_IMG_URL + ApiFactory.BIG_POSTER_SIZE + posterPath

    fun getFullBackDropPosterPath(): String =
        ApiFactory.BASE_IMG_URL + ApiFactory.BIG_POSTER_SIZE + backDropPath
}