package com.example.mymovies.utils

import android.net.Uri
import android.util.Log
import com.example.mymovies.listener.OnStartLoadingListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class NetworkUtils {

    companion object {
        private const val API_KEY = "a8d15abd143f64da04630e5c490dc134"
        private const val BASE_URL = "https://api.themoviedb.org/3/discover/movie"
        private const val BASE_VIDEO_URL = "https://api.themoviedb.org/3/movie/%s/videos"
        private const val BASE_REVIEWS_URL = "https://api.themoviedb.org/3/movie/%s/reviews"

        private const val PARAMS_API_KEY = "api_key"
        private const val PARAMS_LANGUAGE = "language"
        private const val PARAMS_SORT_BY = "sort_by"
        private const val PARAMS_PAGE = "page"
        private const val PARAMS_MIN_VOTE_COUNT = "vote_count.gte"

        private const val SORT_BY_POPULARITY = "popularity.desc"
        private const val SORT_BY_TOP_RATED = "vote_average.desc"
        private const val MIN_VOTE_COUNT_VALUE = "1000"

        var onStartLoadingListener: OnStartLoadingListener? = null

        fun buildURL(page: Int, switch: Boolean, language: String): URL {
            val switchSort = if (switch) {
                SORT_BY_TOP_RATED
            } else {
                SORT_BY_POPULARITY
            }

            val uri = Uri.parse(BASE_URL).buildUpon()
                .appendQueryParameter(PARAMS_API_KEY, API_KEY)
                .appendQueryParameter(PARAMS_LANGUAGE, language)
                .appendQueryParameter(PARAMS_SORT_BY, switchSort)
                .appendQueryParameter(PARAMS_MIN_VOTE_COUNT, MIN_VOTE_COUNT_VALUE)
                .appendQueryParameter(PARAMS_PAGE, page.toString())
                .build()
            return URL(uri.toString())
        }

        fun buildUrlTrailer(idMovie: Int, language: String): URL {
            val uri = Uri.parse(BASE_VIDEO_URL).buildUpon()
                .appendQueryParameter(PARAMS_API_KEY, API_KEY)
                .appendQueryParameter(PARAMS_LANGUAGE, language)
                .build()
            return URL(String.format(uri.toString(), idMovie))
        }

        fun buildUrlReviews(idMovie: Int, language: String): URL {
            val uri = Uri.parse(String.format(BASE_REVIEWS_URL, idMovie)).buildUpon()
                .appendQueryParameter(PARAMS_API_KEY, API_KEY)
                .appendQueryParameter(PARAMS_LANGUAGE, language)
                .build()
            return URL(uri.toString())
        }

        suspend fun loadJSONFromNetwork(vararg urls: URL): JSONObject? {
            return withContext(Dispatchers.IO) {

                onStartLoadingListener?.onStartLoading()

                val stringBuilder = StringBuilder()
                val httpsUrlConnections: HttpsURLConnection?
                var jsonObject: JSONObject? = null
                try {
                    httpsUrlConnections = urls[0].openConnection() as HttpsURLConnection?
                    val inputStream = httpsUrlConnections?.inputStream
                    val inputStreamReader = InputStreamReader(inputStream)
                    val reader = BufferedReader(inputStreamReader)
                    var line = reader.readLine()
                    while (line != null) {
                        stringBuilder.append(line)
                        line = reader.readLine()
                    }
                    if (stringBuilder.isNotEmpty()) {
                        jsonObject = JSONObject(stringBuilder.toString())
                    }
                } catch (e: Exception) {
                    Log.e("Error load Json network", e.message.toString())
                }
                jsonObject
            }
        }
    }
}