package com.andriukhov.mymovies.pojo

import com.andriukhov.mymovies.data.Genre
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class GenreResponse(
    @SerializedName("genres")
    @Expose
    val genres: List<Genre>
)