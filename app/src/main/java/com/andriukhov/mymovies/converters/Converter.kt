package com.andriukhov.mymovies.converters

import androidx.room.TypeConverter
import com.google.gson.Gson

class Converter {

    @TypeConverter
    fun convertToString(genreIds: List<Int>): String {
        return Gson().toJson(genreIds)
    }

    @TypeConverter
    fun convertToList(genreIdsString: String): List<Int> {
        val gson = Gson()
        val objects = gson.fromJson(genreIdsString, ArrayList::class.java)
        val genres = mutableListOf<Int>()
        repeat(objects.size) {
            genres.add(gson.fromJson(it.toString(), Int::class.java))
        }
        return genres
    }
}