package com.example.mymovies.utils

import com.example.mymovies.data.Movie
import com.example.mymovies.data.Review
import com.example.mymovies.data.Trailer
import org.json.JSONObject

class JSONUtils {

    companion object {
        private const val KEY_RESULTS = "results"

        private const val KEY_AUTHOR = "author"
        private const val KEY_CONTENT = "content"

        private const val KEY_NAME = "name"
        private const val KEY_KEY = "key"
        private const val BASE_URL_YOUTUBE = "https://www.youtube.com/watch?v="

        private const val KEY_VOTE_COUNT = "vote_count"
        private const val KEY_ID = "id"
        private const val KEY_TITLE = "title"
        private const val KEY_ORIGINAL_TITLE = "original_title"
        private const val KEY_OVERVIEW = "overview"
        private const val KEY_POSTER_PATH = "poster_path"
        private const val KEY_BACKDROP_PATH = "backdrop_path"
        private const val KEY_VOTE_AVERAGE = "vote_average"
        private const val KEY_RELEASE_DATE = "release_date"
        private const val BASE_URL_IMG = "https://image.tmdb.org/t/p"
        private const val SMALL_POSTER_SIZE = "/w185"
        private const val BIG_POSTER_SIZE = "/w780"

        fun getListMoviesFromJSON(jsonObject: JSONObject?): List<Movie> {
            val listMovies = mutableListOf<Movie>()
            if (jsonObject == null) return listMovies

            val jsonArray = jsonObject.getJSONArray(KEY_RESULTS)

            for (i in 0 until jsonArray.length()) {
                val objectMovie = jsonArray.getJSONObject(i)
                val id = objectMovie.getInt(KEY_ID)
                val voteCount = objectMovie.getInt(KEY_VOTE_COUNT)
                val title = objectMovie.getString(KEY_TITLE)
                val originalTitle = objectMovie.getString(KEY_ORIGINAL_TITLE)
                val overview = objectMovie.getString(KEY_OVERVIEW)
                val posterPath = objectMovie.getString(KEY_POSTER_PATH)
                val backdropPath = objectMovie.getString(KEY_BACKDROP_PATH)
                val voteAverage = objectMovie.getDouble(KEY_VOTE_AVERAGE)
                val releaseDate = objectMovie.getString(KEY_RELEASE_DATE)
                listMovies.add(
                    Movie(
                        id,
                        voteCount,
                        title,
                        originalTitle,
                        overview,
                        BASE_URL_IMG + SMALL_POSTER_SIZE + posterPath,
                        BASE_URL_IMG + BIG_POSTER_SIZE + posterPath,
                        backdropPath,
                        voteAverage,
                        releaseDate
                    )
                )
            }
            return listMovies
        }

        fun getListReviewsFromJson(jsonObject: JSONObject?): List<Review> {
            val listReviews = mutableListOf<Review>()
            if (jsonObject == null) return listReviews

            val jsonArray = jsonObject.getJSONArray(KEY_RESULTS)
            for (i in 0 until jsonArray.length()) {
                val reviewObject = jsonArray.getJSONObject(i)
                val author = reviewObject.getString(KEY_AUTHOR)
                val content = reviewObject.getString(KEY_CONTENT)
                listReviews.add(Review(author, content))
            }
            return listReviews
        }

        fun getListTrailersFromJson(jsonObject: JSONObject?): List<Trailer> {
            val listTrailers = mutableListOf<Trailer>()
            if (jsonObject == null) return listTrailers

            val jsonArray = jsonObject.getJSONArray(KEY_RESULTS)
            for (i in 0 until jsonArray.length()) {
                val trailerObject = jsonArray.getJSONObject(i)
                val name = trailerObject.getString(KEY_NAME)
                val key = BASE_URL_YOUTUBE + trailerObject.getString(KEY_KEY)
                listTrailers.add(Trailer(name, key))
            }
            return listTrailers
        }
    }
}