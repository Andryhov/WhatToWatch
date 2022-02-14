package com.andriukhov.mymovies.response

import com.andriukhov.mymovies.domain.pojo.Actor
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ActorsResponse(
    @SerializedName("cast")
    @Expose
    val actors: List<Actor>
)