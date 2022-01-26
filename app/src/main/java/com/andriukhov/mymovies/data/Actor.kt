package com.andriukhov.mymovies.data

import com.andriukhov.mymovies.api.ApiFactory
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Actor(
    @SerializedName("name")
    @Expose
    val name: String,
    @SerializedName("character")
    @Expose
    val character: String,
    @SerializedName("profile_path")
    @Expose
    val profilePhoto: String
) {
    fun getFullProfilePhotoPath(): String =
        ApiFactory.BASE_IMG_URL + ApiFactory.SMALL_POSTER_SIZE + profilePhoto

    fun getUrlForWebSearchActor(): String =
        ApiFactory.BASE_GOOGLE_SEARCH_URL + name
}