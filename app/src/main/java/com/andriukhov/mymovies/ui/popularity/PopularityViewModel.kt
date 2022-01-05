package com.andriukhov.mymovies.ui.popularity

import android.util.Log
import androidx.lifecycle.*
import com.andriukhov.mymovies.data.Movie
import com.andriukhov.mymovies.repository.MoviesRepository
import kotlinx.coroutines.launch
import java.net.UnknownHostException

class PopularityViewModel(private val repository: MoviesRepository) : ViewModel() {

    var cacheListMovie = mutableListOf<Movie>()

    var exception = MutableLiveData<Throwable>()

    private val dataMovies = MutableLiveData<List<Movie>>()

    val allMovies = repository.getAllMovies.asLiveData()

    fun getPopularityMovies(language: String, page: Int): LiveData<List<Movie>> {
        loadMovies(language, page)
        return dataMovies
    }

    private fun loadMovies(language: String, page: Int) {
        viewModelScope.launch {
            try {
                dataMovies.value = repository.getPopularityMovies(language, page).results
            } catch (e: UnknownHostException) {
                exception.value = e
                Log.i("Network error", e.message.toString())
            }
        }
    }

    fun addMovie(movie: Movie) = viewModelScope.launch {
        repository.insertMovie(movie)
    }

    fun removeMovie(movie: Movie) = viewModelScope.launch {
        repository.deleteMovie(movie)
    }

    fun removeAllMovies() = viewModelScope.launch {
        repository.deleteAllMovies()
    }

    class PopularityViewModelFactory(private val repository: MoviesRepository) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(PopularityViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return PopularityViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}