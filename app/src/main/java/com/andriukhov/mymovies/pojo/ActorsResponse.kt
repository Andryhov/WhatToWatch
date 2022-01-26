package com.andriukhov.mymovies.pojo

import com.andriukhov.mymovies.data.Actor
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ActorsResponse(
    @SerializedName("cast")
    @Expose
    val actors: List<Actor>
)