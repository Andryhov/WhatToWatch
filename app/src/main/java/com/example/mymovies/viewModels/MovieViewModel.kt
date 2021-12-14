package com.example.mymovies.viewModels

import androidx.lifecycle.*
import com.example.mymovies.db.Favorite
import com.example.mymovies.db.Movie
import com.example.mymovies.repository.MoviesRepository
import kotlinx.coroutines.launch

class MovieViewModel(private val repository: MoviesRepository) : ViewModel() {

    var cacheListMovie = mutableListOf<Movie>()

    val allMovies = repository.getAllMovies.asLiveData()

    val allFavouriteMovies = repository.getAllFavouriteMovies.asLiveData()

    fun getMovieById(id: Int): LiveData<Movie> = repository.getMovieById(id).asLiveData()

    fun getFavoriteMovieById(id: Int): LiveData<Favorite> {
        return repository.getFavouriteMovieById(id).asLiveData()
    }

    fun addMovie(movie: Movie) = viewModelScope.launch {
        repository.insertMovie(movie)
    }

    fun addFavouriteMovie(favorite: Favorite) = viewModelScope.launch {
        repository.insertFavouriteMovie(favorite)
    }

    fun removeMovie(movie: Movie) = viewModelScope.launch {
        repository.deleteMovie(movie)
    }

    fun removeFavouriteMovie(favorite: Favorite) = viewModelScope.launch {
        repository.deleteFavouriteMovie(favorite)
    }

    fun removeAllMovies() = viewModelScope.launch {
        repository.deleteAllMovies()
    }
}

class MovieViewModelFactory(private val repository: MoviesRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MovieViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MovieViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}