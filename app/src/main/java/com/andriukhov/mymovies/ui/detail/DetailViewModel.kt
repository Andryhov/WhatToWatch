package com.andriukhov.mymovies.ui.detail

import androidx.lifecycle.*
import com.andriukhov.mymovies.data.Favorite
import com.andriukhov.mymovies.data.Movie
import com.andriukhov.mymovies.data.Review
import com.andriukhov.mymovies.data.Trailer
import com.andriukhov.mymovies.repository.MoviesRepository
import kotlinx.coroutines.launch

class DetailViewModel(private val repository: MoviesRepository) : ViewModel() {

    private val dataReviews = MutableLiveData<List<Review>>()

    private val dataTrailers = MutableLiveData<List<Trailer>>()

    fun getTrailers(id:Int, language: String): LiveData<List<Trailer>> {
        loadTrailers(id, language)
        return dataTrailers
    }

    private fun loadTrailers(id:Int, language: String) {
        viewModelScope.launch {
            dataTrailers.value = repository.getTrailers(id, language).results
        }
    }

    fun getReviews(id: Int, language: String): LiveData<List<Review>> {
        loadReviews(id, language)
        return dataReviews
    }

    private fun loadReviews(id: Int, language: String) {
        viewModelScope.launch {
            dataReviews.value = repository.getReviews(id, language).results
        }
    }

    fun getFavoriteMovieById(id: Int): LiveData<Favorite> {
        return repository.getFavouriteMovieById(id).asLiveData()
    }

    fun addFavouriteMovie(favorite: Favorite) = viewModelScope.launch {
        repository.insertFavouriteMovie(favorite)
    }

    fun getMovieById(id: Int): LiveData<Movie> = repository.getMovieById(id).asLiveData()

    fun removeFavouriteMovie(favorite: Favorite) = viewModelScope.launch {
        repository.deleteFavouriteMovie(favorite)
    }

    class DetailViewModelFactory(private val repository: MoviesRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(DetailViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return DetailViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}