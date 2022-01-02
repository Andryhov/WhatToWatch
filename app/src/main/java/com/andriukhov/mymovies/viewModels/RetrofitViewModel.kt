package com.andriukhov.mymovies.viewModels

import androidx.lifecycle.*
import com.andriukhov.mymovies.api.ApiHelper
import com.andriukhov.mymovies.data.Movie
import com.andriukhov.mymovies.data.Review
import com.andriukhov.mymovies.data.Trailer
import com.andriukhov.mymovies.repository.RetrofitRepository
import kotlinx.coroutines.launch

class RetrofitViewModel(private val repository: RetrofitRepository) : ViewModel() {

    private val dataReviews = MutableLiveData<List<Review>>()

    private val dataTrailers = MutableLiveData<List<Trailer>>()

    private val dataMovies = MutableLiveData<List<Movie>>()

    fun getPopularityMovies(language: String, page: Int): LiveData<List<Movie>> {
        loadPopularityMovies(language, page)
        return dataMovies
    }

    fun getTopRatedMovies(language: String, page: Int): LiveData<List<Movie>> {
        loadTopRatedMovies(language, page)
        return dataMovies
    }

    private fun loadPopularityMovies(language: String, page: Int) {
        viewModelScope.launch {
            dataMovies.value = repository.getPopularityMovies(language, page).results
        }
    }

    private fun loadTopRatedMovies(language: String, page: Int) {
        viewModelScope.launch {
            dataMovies.value = repository.getTopRatedMovies(language, page).results
        }
    }

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
}

class RetrofitViewModelFactory(private val apiHelper: ApiHelper) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RetrofitViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return RetrofitViewModel(RetrofitRepository(apiHelper)) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}