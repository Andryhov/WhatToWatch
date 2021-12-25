package com.andriukhov.mymovies.ui.popularity

import androidx.lifecycle.*
import com.andriukhov.mymovies.data.Movie
import com.andriukhov.mymovies.repository.MoviesRepository
import kotlinx.coroutines.launch

class PopularityViewModel(private val repository: MoviesRepository) : ViewModel() {

    var cacheListMovie = mutableListOf<Movie>()

    val allMovies = repository.getAllMovies.asLiveData()

    fun addMovie(movie: Movie) = viewModelScope.launch {
        repository.insertMovie(movie)
    }

    fun removeMovie(movie: Movie) = viewModelScope.launch {
        repository.deleteMovie(movie)
    }

    fun removeAllMovies() = viewModelScope.launch {
        repository.deleteAllMovies()
    }

    class PopularityViewModelFactory(private val repository: MoviesRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(PopularityViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return PopularityViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}