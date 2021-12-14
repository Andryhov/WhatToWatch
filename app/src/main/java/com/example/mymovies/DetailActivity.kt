package com.example.mymovies

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mymovies.adapter.ReviewsRecyclerViewAdapter
import com.example.mymovies.adapter.TrailersRecycleViewAdapter
import com.example.mymovies.databinding.ActivityDetailBinding
import com.example.mymovies.db.Favorite
import com.example.mymovies.db.Movie
import com.example.mymovies.db.Review
import com.example.mymovies.db.Trailer
import com.example.mymovies.listener.TrailerClickListener
import com.example.mymovies.utils.NetworkUtils
import com.example.mymovies.viewModels.MovieViewModel
import com.example.mymovies.viewModels.MovieViewModelFactory
import com.example.mymovies.viewModels.MoviesManagerViewModel
import com.example.mymovies.viewModels.NetworkViewModelFactory
import com.squareup.picasso.Picasso
import java.util.*

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private var favouriteMovie: Favorite? = null
    private lateinit var movie: Movie
    private var from = ""

    companion object {
        private lateinit var lang: String
    }

    private val moviesViewModel: MovieViewModel by viewModels {
        MovieViewModelFactory((application as MoviesApplication).moviesRepository)
    }

    private val gettingMoviesManagerViewModel: MoviesManagerViewModel by viewModels {
        NetworkViewModelFactory()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.itemMain -> startActivity(Intent(this, MainActivity::class.java))
            R.id.itemFavorite -> startActivity(Intent(this, FavoriteMovieActivity::class.java))
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        lang = Locale.getDefault().language

        val trailerRecycleView = binding.movieInfo.recyclerViewTrailers
        val adapterTrailer = TrailersRecycleViewAdapter()
        trailerRecycleView.adapter = adapterTrailer
        trailerRecycleView.layoutManager = LinearLayoutManager(this@DetailActivity)

        clickOnTrailer(adapterTrailer)

        val reviewRecyclerView = binding.movieInfo.recyclerViewReviews
        val adapterReview = ReviewsRecyclerViewAdapter()
        reviewRecyclerView.adapter = adapterReview
        reviewRecyclerView.layoutManager = LinearLayoutManager(this)


        val id = getParentIntent()
        checkMovieLoadFrom(id)
        gettingMoviesManagerViewModel.getListReviews(NetworkUtils.buildUrlReviews(id, lang))
        gettingMoviesManagerViewModel.getTrailers(NetworkUtils.buildUrlTrailer(id, lang))
        loadTrailers(adapterTrailer)
        loadReviewsForMovie(adapterReview)
        clickFavoriteStar()
    }

    private fun clickOnTrailer(adapterTrailer: TrailersRecycleViewAdapter) {
        adapterTrailer.trailerClickListener = object : TrailerClickListener {
            override fun onTrailerClickListener(position: Int) {
                val trailer = adapterTrailer.trailersList[position]

                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(trailer.key))
                startActivity(intent)
            }
        }
    }

    private fun loadTrailers(adapter: TrailersRecycleViewAdapter) {
        gettingMoviesManagerViewModel.listTrailers.observe(this, {
            if (it.isNotEmpty()) {
                binding.movieInfo.textViewTitleTrailer.visibility = View.VISIBLE
                adapter.trailersList = it as MutableList<Trailer>
            } else {
                binding.movieInfo.textViewTitleTrailer.visibility = View.INVISIBLE
            }
        })
    }

    private fun loadReviewsForMovie(adapter: ReviewsRecyclerViewAdapter) {
        gettingMoviesManagerViewModel.listReviews.observe(this, {
            if (it.isNotEmpty()) {
                binding.movieInfo.textViewTitleReviews.visibility = View.VISIBLE
                adapter.reviewsList = it as MutableList<Review>
            } else {
                binding.movieInfo.textViewTitleReviews.visibility = View.INVISIBLE
            }
        })
    }

    private fun clickFavoriteStar() {
        binding.imageViewFavoriteStar.setOnClickListener {
            if (favouriteMovie == null) {
                moviesViewModel.addFavouriteMovie(Favorite(movie))
                binding.imageViewFavoriteStar.setImageResource(R.drawable.favourite_remove)
                Toast.makeText(this, "Добавлено в избранное", Toast.LENGTH_SHORT).show()
            } else {
                moviesViewModel.removeFavouriteMovie(favouriteMovie as Favorite)
                Toast.makeText(
                    this,
                    "Удалено из избранного",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun getParentIntent(): Int {
        var id = -1
        if (intent != null && intent.hasExtra("id")) {
            id = intent.getIntExtra("id", -1)
            from = intent.getStringExtra("from") ?: ""
        } else {
            finish()
        }
        return id
    }

    private fun checkMovieLoadFrom(id: Int) {
        if (from == "favorite") {
            loadDataOfFavoriteMovie(id)
        } else {
            loadDataOfMovies(id)
        }
    }

    private fun loadDataOfFavoriteMovie(movieId: Int) {
        val movie = moviesViewModel.getFavoriteMovieById(movieId)
        movie.observe(this, {
            setMovieData(it, movieId)
        })
    }

    private fun loadDataOfMovies(id: Int) {
        val movie = moviesViewModel.getMovieById(id)
        movie.observe(this, {
            setMovieData(it, id)
        })
    }

    private fun setMovieData(it: Movie?, id: Int) {
        if (it != null) {
            this.movie = it
            Picasso.get().load(it.bigPosterPath).placeholder(R.drawable.placeholder)
                .into(binding.imageViewBigPoster)
            with(it) {
                binding.movieInfo.textViewNameText.text = title
                binding.movieInfo.textViewOriginalTitleText.text = originalTitle
                binding.movieInfo.textViewRatingText.text = voteAverage.toString()
                binding.movieInfo.textViewReleaseDataText.text = releaseDate
                binding.movieInfo.textViewDescriptionText.text = overview
                loadFavoriteStatus(id)
            }
        }
    }

    private fun loadFavoriteStatus(id: Int): Boolean {
        var status = false
        moviesViewModel.getFavoriteMovieById(id).observe(this, {
            if (it != null) {
                favouriteMovie = it
                binding.imageViewFavoriteStar.setImageResource(R.drawable.favourite_remove)
                status = true
            } else {
                favouriteMovie = it
                binding.imageViewFavoriteStar.setImageResource(R.drawable.favourite_add_to)
            }
        })
        return status
    }
}