package com.andriukhov.mymovies.ui.favorite

import androidx.lifecycle.*
import com.andriukhov.mymovies.repository.MoviesRepository

class FavoriteViewModel(private val repository: MoviesRepository) : ViewModel() {

    val allFavouriteMovies = repository.getAllFavouriteMovies.asLiveData()

    class FavouriteViewModelFactory(private val repository: MoviesRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(FavoriteViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return FavoriteViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}