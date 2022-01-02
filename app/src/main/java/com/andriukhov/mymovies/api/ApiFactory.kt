package com.andriukhov.mymovies.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiFactory {
    companion object {
        private var apiFactory: ApiFactory? = null
        private lateinit var retrofit: Retrofit
        private const val BASE_URL = "https://api.themoviedb.org/3/"

        fun getInstance(): ApiFactory? {
            if(apiFactory == null) {
                apiFactory = ApiFactory()
            }
            return apiFactory
        }
    }

    init {
        retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
    }

    fun getApiService(): ApiService {
        return retrofit.create(ApiService::class.java)
    }
}