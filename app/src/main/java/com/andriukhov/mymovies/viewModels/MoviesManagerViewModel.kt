package com.andriukhov.mymovies.viewModels

import androidx.lifecycle.*
import com.andriukhov.mymovies.data.Movie
import com.andriukhov.mymovies.data.Review
import com.andriukhov.mymovies.data.Trailer
import com.andriukhov.mymovies.utils.JSONUtils
import com.andriukhov.mymovies.utils.NetworkUtils
import kotlinx.coroutines.launch
import java.net.URL

class MoviesManagerViewModel : ViewModel() {

    private val _listMovies = MutableLiveData<List<Movie>>()
    val listMovies: LiveData<List<Movie>> = _listMovies

    private val _listReviews = MutableLiveData<List<Review>>()
    val listReviews: LiveData<List<Review>> = _listReviews

    private val _listTrailers = MutableLiveData<List<Trailer>>()
    val listTrailers: LiveData<List<Trailer>> = _listTrailers

    fun loadMoviesList(url: URL) {
        viewModelScope.launch {
            _listMovies.postValue(
                JSONUtils.getListMoviesFromJSON(
                    NetworkUtils.loadJSONFromNetwork(
                        url
                    )
                )
            )
        }
    }

    fun getListReviews(url: URL) {
        viewModelScope.launch {
            _listReviews.value =
                JSONUtils.getListReviewsFromJson(NetworkUtils.loadJSONFromNetwork(url))
        }
    }

    fun getTrailers(url: URL) {
        viewModelScope.launch {
            _listTrailers.value =
                JSONUtils.getListTrailersFromJson(NetworkUtils.loadJSONFromNetwork(url))
        }
    }
}

class NetworkViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MoviesManagerViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MoviesManagerViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}