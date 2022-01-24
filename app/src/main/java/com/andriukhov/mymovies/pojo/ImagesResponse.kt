package com.andriukhov.mymovies.pojo

import com.andriukhov.mymovies.data.Image
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ImagesResponse(
    @SerializedName("backdrops")
    @Expose
    val images: List<Image>
)