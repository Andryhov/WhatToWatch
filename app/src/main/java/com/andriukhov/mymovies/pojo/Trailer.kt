package com.andriukhov.mymovies.pojo

import com.andriukhov.mymovies.api.ApiFactory
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Trailer(
    @SerializedName("name")
    @Expose
    val name: String,
    @SerializedName("key")
    @Expose
    val key: String
) {
    fun getFullTrailerPath(): String = ApiFactory.BASE_URL_YOUTUBE + key
}