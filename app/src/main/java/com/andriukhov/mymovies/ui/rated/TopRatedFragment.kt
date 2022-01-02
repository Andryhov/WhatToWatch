package com.andriukhov.mymovies.ui.rated

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
import com.andriukhov.mymovies.MoviesApplication
import com.andriukhov.mymovies.R
import com.andriukhov.mymovies.adapter.MoviesRecyclerViewAdapter
import com.andriukhov.mymovies.api.ApiFactory
import com.andriukhov.mymovies.api.ApiHelper
import com.andriukhov.mymovies.databinding.FragmentTopratedBinding
import com.andriukhov.mymovies.data.Movie
import com.andriukhov.mymovies.listener.OnReachEndListener
import com.andriukhov.mymovies.listener.PosterClickListener
import com.andriukhov.mymovies.viewModels.RetrofitViewModel
import com.andriukhov.mymovies.viewModels.RetrofitViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class TopRatedFragment : Fragment() {

    private lateinit var ratedViewModel: RatedViewModel
    private lateinit var networkViewModel: RetrofitViewModel
    private var _binding: FragmentTopratedBinding? = null
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
        _binding = FragmentTopratedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("page", page)
        ratedViewModel.cacheListMovie.addAll(adapter.listMovies)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        lang = Locale.getDefault().language
        val recyclerView = binding.recyclerViewPosters
        recyclerView.adapter = adapter
        recyclerView.layoutManager = GridLayoutManager(this.context, getColumnCount())
        getMovies()
        reachEndListener()
        clickOnPoster()

        if (savedInstanceState != null) {
            page = savedInstanceState.getInt("page")
            adapter.listMovies.addAll(ratedViewModel.cacheListMovie)
            ratedViewModel.cacheListMovie.clear()
        }
    }

    private fun initViewModel() {
        ratedViewModel =
            ViewModelProvider(
                this,
                RatedViewModel.RatedViewModelFactory(
                    (requireNotNull(this.activity).application as MoviesApplication)
                        .moviesRepository
                )
            )[RatedViewModel::class.java]

        networkViewModel =
            ViewModelProvider(
                this,
                RetrofitViewModelFactory(ApiHelper(ApiFactory.getInstance()!!.getApiService()))
            )[RetrofitViewModel::class.java]
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
        ratedViewModel.cacheListMovie.clear()
    }

    private fun getColumnCount(): Int {
        val displayMetrics = resources.displayMetrics
        val fl = (displayMetrics.widthPixels / displayMetrics.density).toInt()
        return if (fl / 185 > 2) fl / 185 else 2
    }

    private fun loadMoviesFromDb() {
        ratedViewModel.allMovies.observe(viewLifecycleOwner, {
            if (page == 1) {
                this.adapter.listMovies = it as MutableList<Movie>
            }
        })
    }

    private fun reachEndListener() {
        adapter.onReachEndListener = object : OnReachEndListener {
            override fun onReachEnd() {
                if (!isLoading) {
                    isLoading = true
                    getMovies()
                    binding.progressBarLoading.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun getMovies() {
        networkViewModel.getTopRatedMovies(lang, page).observe(viewLifecycleOwner, {
            if (it.isNotEmpty()) {
                if (ratedViewModel.cacheListMovie.size == 0 && !adapter.listMovies.containsAll(it)) {
                    clearMoviesFromAll()
                    it.forEach { movie -> ratedViewModel.addMovie(movie) }
                    adapter.listMovies = it as MutableList<Movie>
                    page++
                }
            } else {
                loadMoviesFromDb()
                Toast.makeText(
                    this.context,
                    getString(R.string.no_internet_connection),
                    Toast.LENGTH_SHORT
                ).show()
            }
            isLoading = false
            lifecycleScope.launch(Dispatchers.Main) {
                binding.progressBarLoading.visibility = View.INVISIBLE
            }
        })
    }

    private fun clearMoviesFromAll() {
        if (page == 1) {
            adapter.clear()
            ratedViewModel.removeAllMovies()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}