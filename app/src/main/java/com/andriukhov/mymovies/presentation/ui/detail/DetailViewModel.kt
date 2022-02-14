package com.andriukhov.mymovies.presentation.ui.detail

import android.util.Log
import androidx.lifecycle.*
import com.andriukhov.mymovies.domain.pojo.*
import com.andriukhov.mymovies.repository.MoviesRepository
import kotlinx.coroutines.launch
import java.net.UnknownHostException
import java.util.*

class DetailViewModel(private val repository: MoviesRepository) : ViewModel() {

    private val dataReviews = MutableLiveData<List<Review>>()

    private val dataTrailers = MutableLiveData<List<Trailer>>()

    private val dataGenresName = MutableLiveData<List<String>>()

    private val dataImages = MutableLiveData<List<Image>>()

    private val dataActors = MutableLiveData<List<Actor>>()

    private val language = Locale.getDefault().language

    fun getTrailers(id: Int): LiveData<List<Trailer>> {
        loadTrailers(id)
        return dataTrailers
    }

    private fun loadTrailers(id: Int) {
        viewModelScope.launch {
            try {
                dataTrailers.value = repository.getTrailers(id, language).results
            } catch (e: UnknownHostException) {
                Log.i("Trailer load error", e.message.toString())
            }
        }
    }

    fun getReviews(id: Int): LiveData<List<Review>> {
        loadReviews(id)
        return dataReviews
    }

    private fun loadReviews(id: Int) {
        viewModelScope.launch {
            try {
                dataReviews.value = repository.getReviews(id, language).results
            } catch (e: UnknownHostException) {
                Log.i("Reviews load error", e.message.toString())
            }
        }
    }

    fun getGenresName(list: List<Int>): LiveData<List<String>> {
        loadGenres(list)
        return dataGenresName
    }

    private fun loadGenres(list: List<Int>) {
        viewModelScope.launch {
            try {
                val genres: List<Genre> = repository.loadGenres(language).genres
                val listGeneresName = mutableListOf<String>()
                if (!genres.isNullOrEmpty()) {
                    list.forEach { id ->
                        listGeneresName.add(genres.last { genre -> genre.id == id }.name)
                    }
                }
                dataGenresName.value = listGeneresName
            } catch (e: UnknownHostException) {
                dataGenresName.value = listOf()
                Log.e("Genres load error", e.message.toString())
            }
        }
    }

    fun getImages(id: Int): LiveData<List<Image>> {
        loadImages(id)
        return dataImages
    }

    private fun loadImages(id: Int) {
        viewModelScope.launch {
            try {
                dataImages.value = repository.loadImages(id, language).images
            } catch (e: UnknownHostException) {
                dataImages.value = listOf()
            }
        }
    }

    fun getActors(id: Int): LiveData<List<Actor>> {
        loadActors(id)
        return dataActors
    }

    private fun loadActors(id: Int) {
        viewModelScope.launch {
            try {
                dataActors.value = repository.loadActors(id, language).actors
            } catch (e: UnknownHostException) {
                dataActors.value = listOf()
            }
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

    class DetailViewModelFactory(private val repository: MoviesRepository) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(DetailViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return DetailViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}