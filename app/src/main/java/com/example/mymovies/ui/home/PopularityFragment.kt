package com.example.mymovies.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.mymovies.MoviesApplication
import com.example.mymovies.R
import com.example.mymovies.adapter.MoviesRecyclerViewAdapter
import com.example.mymovies.databinding.FragmentPopularityBinding
import com.example.mymovies.db.Movie
import com.example.mymovies.listener.OnReachEndListener
import com.example.mymovies.listener.OnStartLoadingListener
import com.example.mymovies.listener.PosterClickListener
import com.example.mymovies.utils.NetworkUtils
import com.example.mymovies.viewModels.MoviesManagerViewModel
import com.example.mymovies.viewModels.NetworkViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class PopularityFragment : Fragment() {

    private var moviesViewModel: PopularityViewModel? = null
    private lateinit var networkViewModel: MoviesManagerViewModel
    private var _binding: FragmentPopularityBinding? = null
    private var adapter = MoviesRecyclerViewAdapter()

    private var page = 1
    private var isLoading = false

    companion object {
        private lateinit var lang: String
    }

    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = MoviesRecyclerViewAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPopularityBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("page", page)
        moviesViewModel?.cacheListMovie?.addAll(adapter.listMovies)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        moviesViewModel =
            ViewModelProvider(
                this,
                PopularityViewModel.PopularityViewModelFactory(
                    (requireNotNull(this.activity).application as MoviesApplication)
                        .moviesRepository
                )
            )[PopularityViewModel::class.java]

        networkViewModel =
            ViewModelProvider(this, NetworkViewModelFactory())[MoviesManagerViewModel::class.java]

        lang = Locale.getDefault().language

        val recyclerView = binding.recyclerViewPosters
        recyclerView.adapter = adapter
        recyclerView.layoutManager = GridLayoutManager(this.context, getColumnCount())
        observeNetworkView()
        reachEndListener()
        loadDataFromNetwork(page)
        clickOnPoster()

        if (savedInstanceState != null) {
            page = savedInstanceState.getInt("page")
            adapter.listMovies.addAll(moviesViewModel?.cacheListMovie ?: listOf())
            moviesViewModel?.cacheListMovie?.clear()
        }
    }

    private fun clickOnPoster() {
        adapter.posterClickListener = object : PosterClickListener {
            override fun onPosterClickListener(position: Int) {
                val movie = adapter.listMovies[position]
                val bundle = Bundle()
                bundle.putInt("id", movie.id)
                findNavController().navigate(R.id.detail_fragment, bundle)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        isLoading = false
        moviesViewModel?.cacheListMovie?.clear()
    }

    private fun getColumnCount(): Int {
        val displayMetrics = resources.displayMetrics
        val fl = (displayMetrics.widthPixels / displayMetrics.density).toInt()
        return if (fl / 185 > 2) fl / 185 else 2
    }

    private fun observeMovies() {
        moviesViewModel?.allMovies?.observe(viewLifecycleOwner, {
            if (page == 1) {
                this.adapter.listMovies = it as MutableList<Movie>
            }
        })
    }

    private fun reachEndListener() {
        adapter.onReachEndListener = object : OnReachEndListener {
            override fun onReachEnd() {
                if (!isLoading) {
                    loadDataFromNetwork(page)
                }
            }
        }
    }

    private fun loadDataFromNetwork(page: Int) {
        if (moviesViewModel?.cacheListMovie?.size == 0) {
            networkViewModel.loadMoviesList(NetworkUtils.buildURL(page, false, lang))
        }
        NetworkUtils.onStartLoadingListener = object : OnStartLoadingListener {
            override fun onStartLoading() {
                isLoading = true
            }
        }
        binding.progressBarLoading.visibility = View.VISIBLE
    }

    private fun observeNetworkView() {
        networkViewModel.listMovies.observe(viewLifecycleOwner, {
            if (it.isNotEmpty()) {
                if (moviesViewModel?.cacheListMovie?.size == 0 && !adapter.listMovies.containsAll(it)) {
                    removeMoviesFromAll()
                    it.forEach { movie -> moviesViewModel?.addMovie(movie) }
                    adapter.listMovies = it as MutableList<Movie>
                    page++
                }
            } else {
                observeMovies()
                Toast.makeText(
                    this.context,
                    "No internet connection, please try again later ...",
                    Toast.LENGTH_SHORT
                ).show()
            }
            isLoading = false
            lifecycleScope.launch(Dispatchers.Main) {
                binding.progressBarLoading.visibility = View.INVISIBLE
            }
        })
    }

    private fun removeMoviesFromAll() {
        if (page == 1) {
            adapter.clear()
            moviesViewModel?.removeAllMovies()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}