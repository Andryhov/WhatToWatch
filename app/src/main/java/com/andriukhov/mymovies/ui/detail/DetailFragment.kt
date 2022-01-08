package com.andriukhov.mymovies.ui.detail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.andriukhov.mymovies.MoviesApplication
import com.andriukhov.mymovies.R
import com.andriukhov.mymovies.adapter.ReviewsRecyclerViewAdapter
import com.andriukhov.mymovies.adapter.TrailersRecycleViewAdapter
import com.andriukhov.mymovies.data.Favorite
import com.andriukhov.mymovies.data.Movie
import com.andriukhov.mymovies.data.Review
import com.andriukhov.mymovies.data.Trailer
import com.andriukhov.mymovies.databinding.DetailFragmentBinding
import com.andriukhov.mymovies.listener.TrailerClickListener
import com.squareup.picasso.Picasso

class DetailFragment : Fragment() {

    private lateinit var binding: DetailFragmentBinding
    private lateinit var viewModel: DetailViewModel
    private var favouriteMovie: Favorite? = null
    private lateinit var movie: Movie
    private var from = ""
    private var idMovie = -1

    companion object {
        //        fun newInstance() = DetailFragment()
        private const val BASE_URL_IMG = "https://image.tmdb.org/t/p"
        private const val BASE_URL_YOUTUBE = "https://www.youtube.com/watch?v="
        private const val BIG_POSTER_SIZE = "/w780"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DetailFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()

        val trailerRecycleView = binding.movieInfo.recyclerViewTrailers
        val adapterTrailer = TrailersRecycleViewAdapter()
        trailerRecycleView.adapter = adapterTrailer
        trailerRecycleView.layoutManager = LinearLayoutManager(this.context)

        val reviewRecyclerView = binding.movieInfo.recyclerViewReviews
        val adapterReview = ReviewsRecyclerViewAdapter()
        reviewRecyclerView.adapter = adapterReview
        reviewRecyclerView.layoutManager = LinearLayoutManager(this.context)

        idMovie = arguments?.getInt("id") ?: -1
        from = arguments?.getString("from") ?: ""

        checkMovieLoadFrom()
        getTrailers(adapterTrailer)
        getReviews(adapterReview)
        clickFavoriteStar()
        clickOnTrailer(adapterTrailer)
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(
            this,
            DetailViewModel.DetailViewModelFactory((requireNotNull(this.activity).application as MoviesApplication).moviesRepository)
        )[DetailViewModel::class.java]
    }

    private fun clickOnTrailer(adapterTrailer: TrailersRecycleViewAdapter) {
        adapterTrailer.trailerClickListener = object : TrailerClickListener {
            override fun onTrailerClickListener(position: Int) {
                val trailer = adapterTrailer.trailersList[position]

                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(BASE_URL_YOUTUBE + trailer.key))
                startActivity(intent)
            }
        }
    }

    private fun getTrailers(adapter: TrailersRecycleViewAdapter) {
        viewModel.getTrailers(idMovie).observe(viewLifecycleOwner, {
            if (it.isNotEmpty()) {
                binding.movieInfo.textViewTitleTrailer.visibility = View.VISIBLE
                adapter.trailersList = it as MutableList<Trailer>
            } else {
                binding.movieInfo.textViewTitleTrailer.visibility = View.INVISIBLE
            }
        })
    }

    private fun getReviews(adapter: ReviewsRecyclerViewAdapter) {
        viewModel.getReviews(idMovie).observe(viewLifecycleOwner, {
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

    private fun checkMovieLoadFrom() {
        if (from == "favorite") {
            loadDataOfFavoriteMovie()
        } else {
            loadDataOfMovies()
        }
    }

    private fun loadDataOfFavoriteMovie() {
        val movie = viewModel.getFavoriteMovieById(idMovie)
        movie.observe(viewLifecycleOwner, {
            setInfoOfMovie(it)
        })
    }

    private fun loadDataOfMovies() {
        val movie = viewModel.getMovieById(idMovie)
        movie.observe(viewLifecycleOwner, {
            setInfoOfMovie(it)
        })
    }

    private fun setInfoOfMovie(it: Movie?) {
        if (it != null) {
            this.movie = it
            Picasso.get().load(BASE_URL_IMG + BIG_POSTER_SIZE + it.posterPath)
                .placeholder(R.drawable.placeholder)
                .into(binding.imageViewBigPoster)
            with(it) {
                binding.movieInfo.textViewNameText.text = title
                binding.movieInfo.textViewOriginalTitleText.text = originalTitle
                binding.movieInfo.textViewRatingText.text = voteAverage.toString()
                binding.movieInfo.textViewReleaseDataText.text = releaseDate
                binding.movieInfo.textViewDescriptionText.text = overview
                setFavoriteStatus(it)
                setGenres(it)
            }
        }
    }

    private fun setGenres(movie: Movie) {
        viewModel.getGenresName(movie.genreIds).observe(viewLifecycleOwner, {
           //todo
        })
    }

    private fun setFavoriteStatus(movie: Movie): Boolean {
        var status = false
        viewModel.getFavoriteMovieById(movie.id).observe(viewLifecycleOwner, {
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