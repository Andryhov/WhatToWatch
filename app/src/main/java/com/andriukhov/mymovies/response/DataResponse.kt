package com.andriukhov.mymovies.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class DataResponse <T> (
    @SerializedName("results")
    @Expose
    val results: List<T>
)