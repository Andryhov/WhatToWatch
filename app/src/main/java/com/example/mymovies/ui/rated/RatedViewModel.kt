package com.example.mymovies.ui.rated

import androidx.lifecycle.*
import com.example.mymovies.data.Movie
import com.example.mymovies.repository.MoviesRepository
import kotlinx.coroutines.launch

class RatedViewModel(private val repository: MoviesRepository) : ViewModel() {

    var cacheListMovie = mutableListOf<Movie>()

    val allMovies = repository.getAllMovies.asLiveData()

    fun getMovieById(id: Int): LiveData<Movie> = repository.getMovieById(id).asLiveData()

    fun addMovie(movie: Movie) = viewModelScope.launch {
        repository.insertMovie(movie)
    }

    fun removeAllMovies() = viewModelScope.launch {
        repository.deleteAllMovies()
    }

    class RatedViewModelFactory(private val repository: MoviesRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(RatedViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return RatedViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}