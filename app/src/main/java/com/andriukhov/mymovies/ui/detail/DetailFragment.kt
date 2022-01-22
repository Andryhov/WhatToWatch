package com.andriukhov.mymovies.ui.detail

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
import com.andriukhov.mymovies.MoviesApplication
import com.andriukhov.mymovies.R
import com.andriukhov.mymovies.adapter.GenreRecyclerViewAdapter
import com.andriukhov.mymovies.adapter.ReviewsRecyclerViewAdapter
import com.andriukhov.mymovies.adapter.TrailersRecycleViewAdapter
import com.andriukhov.mymovies.data.Favorite
import com.andriukhov.mymovies.data.Movie
import com.andriukhov.mymovies.data.Review
import com.andriukhov.mymovies.data.Trailer
import com.andriukhov.mymovies.databinding.DetailFragmentBinding
import com.andriukhov.mymovies.listener.TrailerClickListener
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.squareup.picasso.Picasso

class DetailFragment : Fragment() {

    private lateinit var binding: DetailFragmentBinding
    private lateinit var viewModel: DetailViewModel

    private lateinit var genreAdapter: GenreRecyclerViewAdapter
    private lateinit var trailerAdapter: TrailersRecycleViewAdapter
    private lateinit var reviewAdapter: ReviewsRecyclerViewAdapter

    private var favouriteMovie: Favorite? = null
    private lateinit var movie: Movie
    private var from = ""
    private var idMovie = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        genreAdapter = GenreRecyclerViewAdapter()
        trailerAdapter = TrailersRecycleViewAdapter()
        reviewAdapter = ReviewsRecyclerViewAdapter()
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
        trailerRecycleView.adapter = trailerAdapter
        trailerRecycleView.layoutManager = LinearLayoutManager(this.context)

        val reviewRecyclerView = binding.movieInfo.recyclerViewReviews
        reviewRecyclerView.adapter = reviewAdapter
        reviewRecyclerView.layoutManager = LinearLayoutManager(this.context)

        val genreRecyclerView = binding.movieInfo.recyclerViewGenres
        val layoutManager = FlexboxLayoutManager(this.context)
        layoutManager.flexDirection = FlexDirection.ROW
        layoutManager.justifyContent = JustifyContent.FLEX_START
        genreRecyclerView.adapter = genreAdapter
        genreRecyclerView.layoutManager = layoutManager

        idMovie = arguments?.getInt("id") ?: -1
        from = arguments?.getString("from") ?: ""

        checkMovieLoadFrom()
        getTrailers()
        getReviews()
        clickFavoriteStar()
        clickOnTrailer()
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(
            this,
            DetailViewModel.DetailViewModelFactory((requireNotNull(this.activity).application as MoviesApplication).moviesRepository)
        )[DetailViewModel::class.java]
    }

    private fun clickOnTrailer() {
        trailerAdapter.trailerClickListener = object : TrailerClickListener {
            override fun onTrailerClickListener(position: Int) {
                val trailer = trailerAdapter.trailersList[position]

                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(trailer.getFullTrailerPath()))
                startActivity(intent)
            }
        }
    }

    private fun getTrailers() {
        viewModel.getTrailers(idMovie).observe(viewLifecycleOwner, {
            if (it.isNotEmpty()) {
                binding.movieInfo.textViewTitleTrailer.visibility = View.VISIBLE
                trailerAdapter.trailersList = it as MutableList<Trailer>
            } else {
                binding.movieInfo.textViewTitleTrailer.visibility = View.INVISIBLE
            }
        })
    }

    private fun getReviews() {
        viewModel.getReviews(idMovie).observe(viewLifecycleOwner, {
            if (it.isNotEmpty()) {
                binding.movieInfo.textViewTitleReviews.visibility = View.VISIBLE
                reviewAdapter.reviewsList = it as MutableList<Review>
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
            if (binding.imageViewSmallPoster == null) {
                Picasso.get().load(it.getFullBigPosterPath())
                    .placeholder(R.drawable.placeholder)
                    .into(binding.imageViewBigPoster)
            } else {
                templateImgForLandscape(it)
            }
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

    private fun templateImgForLandscape(movie: Movie) {
        Picasso.get().load(movie.getFullBackDropPosterPath())
            .placeholder(R.drawable.placeholder)
            .into(binding.imageViewBigPoster)
        Picasso.get().load(movie.getFullBigPosterPath())
            .into(binding.imageViewSmallPoster)
    }

    private fun setGenres(movie: Movie) {
        viewModel.getGenresName(movie.genreIds).observe(viewLifecycleOwner, {
            genreAdapter.genreList = it as MutableList<String>
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