package com.andriukhov.mymovies.data

import com.andriukhov.mymovies.api.ApiFactory
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Image(
    @SerializedName("file_path")
    @Expose
    val filePath: String
) {
    fun getFullImagePath(): String =
        ApiFactory.BASE_IMG_URL + ApiFactory.BIG_POSTER_SIZE + filePath
}