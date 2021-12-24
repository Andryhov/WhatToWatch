package com.example.mymovies.ui.detail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mymovies.MoviesApplication
import com.example.mymovies.R
import com.example.mymovies.adapter.ReviewsRecyclerViewAdapter
import com.example.mymovies.adapter.TrailersRecycleViewAdapter
import com.example.mymovies.databinding.DetailFragmentBinding
import com.example.mymovies.data.Favorite
import com.example.mymovies.data.Movie
import com.example.mymovies.data.Review
import com.example.mymovies.data.Trailer
import com.example.mymovies.listener.TrailerClickListener
import com.example.mymovies.utils.NetworkUtils
import com.example.mymovies.viewModels.MoviesManagerViewModel
import com.example.mymovies.viewModels.NetworkViewModelFactory
import com.squareup.picasso.Picasso
import java.util.*

class DetailFragment : Fragment() {

    private lateinit var binding: DetailFragmentBinding
    private lateinit var viewModel: DetailViewModel
    private lateinit var networkViewModel: MoviesManagerViewModel
    private var favouriteMovie: Favorite? = null
    private lateinit var movie: Movie
    private var from = ""

    companion object {
        fun newInstance() = DetailFragment()
        private lateinit var lang: String
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DetailFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(
            this,
            DetailViewModel.DetailViewModelFactory((requireNotNull(this.activity).application as MoviesApplication).moviesRepository)
        )[DetailViewModel::class.java]

        networkViewModel =
            ViewModelProvider(this, NetworkViewModelFactory())[MoviesManagerViewModel::class.java]

        lang = Locale.getDefault().language

        val trailerRecycleView = binding.movieInfo.recyclerViewTrailers
        val adapterTrailer = TrailersRecycleViewAdapter()
        trailerRecycleView.adapter = adapterTrailer
        trailerRecycleView.layoutManager = LinearLayoutManager(this.context)

        clickOnTrailer(adapterTrailer)

        val reviewRecyclerView = binding.movieInfo.recyclerViewReviews
        val adapterReview = ReviewsRecyclerViewAdapter()
        reviewRecyclerView.adapter = adapterReview
        reviewRecyclerView.layoutManager = LinearLayoutManager(this.context)

        val id = arguments?.getInt("id") ?: -1
        from = arguments?.getString("from") ?: ""

        checkMovieLoadFrom(id)
        networkViewModel.getListReviews(NetworkUtils.buildUrlReviews(id, lang))
        networkViewModel.getTrailers(NetworkUtils.buildUrlTrailer(id, lang))
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
        networkViewModel.listTrailers.observe(viewLifecycleOwner, {
            if (it.isNotEmpty()) {
                binding.movieInfo.textViewTitleTrailer.visibility = View.VISIBLE
                adapter.trailersList = it as MutableList<Trailer>
            } else {
                binding.movieInfo.textViewTitleTrailer.visibility = View.INVISIBLE
            }
        })
    }

    private fun loadReviewsForMovie(adapter: ReviewsRecyclerViewAdapter) {
        networkViewModel.listReviews.observe(viewLifecycleOwner, {
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
                viewModel.addFavouriteMovie(Favorite(movie))
                binding.imageViewFavoriteStar.setImageResource(R.drawable.favourite_remove)
                Toast.makeText(
                    this.context,
                    getString(R.string.add_to_favorite),
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                viewModel.removeFavouriteMovie(favouriteMovie as Favorite)
                Toast.makeText(
                    this.context,
                    getString(R.string.remove_from_favorite),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun checkMovieLoadFrom(id: Int) {
        if (from == "favorite") {
            loadDataOfFavoriteMovie(id)
        } else {
            loadDataOfMovies(id)
        }
    }

    private fun loadDataOfFavoriteMovie(movieId: Int) {
        val movie = viewModel.getFavoriteMovieById(movieId)
        movie.observe(viewLifecycleOwner, {
            setMovieData(it, movieId)
        })
    }

    private fun loadDataOfMovies(id: Int) {
        val movie = viewModel.getMovieById(id)
        movie.observe(viewLifecycleOwner, {
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
        viewModel.getFavoriteMovieById(id).observe(viewLifecycleOwner, {
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