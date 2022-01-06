package com.andriukhov.mymovies.ui.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.andriukhov.mymovies.MoviesApplication
import com.andriukhov.mymovies.R
import com.andriukhov.mymovies.adapter.MoviesRecyclerViewAdapter
import com.andriukhov.mymovies.databinding.FragmentFavoriteBinding
import com.andriukhov.mymovies.data.Movie
import com.andriukhov.mymovies.listener.PosterClickListener

class FavoriteFragment : Fragment() {

    private lateinit var favoriteViewModel: FavoriteViewModel
    private var _binding: FragmentFavoriteBinding? = null
    private lateinit var adapter: MoviesRecyclerViewAdapter
    
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = MoviesRecyclerViewAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        val recyclerViewFavorite = binding.recyclerViewFavorite
        recyclerViewFavorite.adapter = adapter
        recyclerViewFavorite.layoutManager = GridLayoutManager(this.context, 2)
        loadFavoriteMovie()
        clickOnPoster()
    }

    private fun initViewModel() {
        favoriteViewModel =
            ViewModelProvider(
                this,
                FavoriteViewModel.FavouriteViewModelFactory((requireNotNull(this.activity).application as MoviesApplication).moviesRepository)
            )[FavoriteViewModel::class.java]
    }

    private fun loadFavoriteMovie() {
        favoriteViewModel.allFavouriteMovies.observe(viewLifecycleOwner, {
            adapter.clear()
            if (it != null) {
                val listMovies = mutableListOf<Movie>()
                listMovies.addAll(it.map { favorite -> Movie(favorite) })
                adapter.listMovies = listMovies
            }
        })
    }

    private fun clickOnPoster() {
        adapter.posterClickListener = object : PosterClickListener {
            override fun onPosterClickListener(position: Int) {
                val movie = adapter.listMovies[position]
                val bundle = Bundle()
                bundle.putInt("id", movie.id)
                bundle.putString("from", "favorite")
                findNavController().navigate(R.id.detail_fragment, bundle)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}