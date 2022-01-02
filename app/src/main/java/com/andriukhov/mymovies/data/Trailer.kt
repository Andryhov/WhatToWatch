package com.andriukhov.mymovies.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Trailer(
    @SerializedName("name")
    @Expose
    val name: String,
    @SerializedName("key")
    @Expose
    val key: String
)