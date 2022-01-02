package com.andriukhov.mymovies.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Response <T> (
    @SerializedName("results")
    @Expose
    val results: List<T>
)