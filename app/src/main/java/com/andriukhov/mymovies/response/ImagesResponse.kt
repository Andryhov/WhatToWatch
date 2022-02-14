package com.andriukhov.mymovies.response

import com.andriukhov.mymovies.domain.pojo.Image
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ImagesResponse(
    @SerializedName("backdrops")
    @Expose
    val images: List<Image>
)