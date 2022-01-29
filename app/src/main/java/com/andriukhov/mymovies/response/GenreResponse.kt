package com.andriukhov.mymovies.response

import com.andriukhov.mymovies.pojo.Genre
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class GenreResponse(
    @SerializedName("genres")
    @Expose
    val genres: List<Genre>
)