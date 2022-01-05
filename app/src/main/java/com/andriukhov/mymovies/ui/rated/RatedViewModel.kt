package com.andriukhov.mymovies.ui.rated

import androidx.lifecycle.*
import com.andriukhov.mymovies.data.Movie
import com.andriukhov.mymovies.repository.MoviesRepository
import kotlinx.coroutines.launch

class RatedViewModel(private val repository: MoviesRepository) : ViewModel() {

    var cacheListMovie = mutableListOf<Movie>()

    private val dataMovies = MutableLiveData<List<Movie>>()

    val allMovies = repository.getAllMovies.asLiveData()

    fun getMovieById(id: Int): LiveData<Movie> = repository.getMovieById(id).asLiveData()

    fun addMovie(movie: Movie) = viewModelScope.launch {
        repository.insertMovie(movie)
    }

    fun removeAllMovies() = viewModelScope.launch {
        repository.deleteAllMovies()
    }

    fun getTopRatedMovies(language: String, page: Int): LiveData<List<Movie>> {
        loadTopRatedMovies(language, page)
        return dataMovies
    }

    private fun loadTopRatedMovies(language: String, page: Int) {
        viewModelScope.launch {
            dataMovies.value = repository.getTopRatedMovies(language, page).results
        }
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