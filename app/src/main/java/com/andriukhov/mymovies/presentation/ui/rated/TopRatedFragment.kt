package com.andriukhov.mymovies.presentation.ui.rated

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
import com.andriukhov.mymovies.presentation.adapter.MoviesRecyclerViewAdapter
import com.andriukhov.mymovies.domain.pojo.Movie
import com.andriukhov.mymovies.databinding.FragmentTopratedBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class TopRatedFragment : Fragment() {

    private var ratedViewModel: RatedViewModel? = null
    private var _binding: FragmentTopratedBinding? = null
    private var adapter = MoviesRecyclerViewAdapter()

    private var page = 1
    private var isLoading = false
    private lateinit var lang: String

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
        _binding = FragmentTopratedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("page", page)
        ratedViewModel?.cacheListMovie?.addAll(adapter.listMovies)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        lang = Locale.getDefault().language
        val recyclerView = binding.recyclerViewPosters
        recyclerView.adapter = adapter
        recyclerView.layoutManager = GridLayoutManager(this.context, getColumnCount())
        loadInstanceState(savedInstanceState)
        getMovies()
        reachEndListener()
        clickOnPoster()
        getException()
    }

    override fun onResume() {
        super.onResume()
        isLoading = false
        ratedViewModel?.cacheListMovie?.clear()
    }

    private fun loadInstanceState(savedInstanceState: Bundle?) {
        if (savedInstanceState != null) {
            page = savedInstanceState.getInt("page")
            ratedViewModel?.let { adapter.listMovies.addAll(it.cacheListMovie) }
            ratedViewModel?.cacheListMovie?.clear()
        }
    }

    private fun initViewModel() {
        ratedViewModel =
            ViewModelProvider(
                this, RatedViewModel.RatedViewModelFactory(
                    (requireNotNull(this.activity).application as MoviesApplication)
                        .moviesRepository
                )
            )[RatedViewModel::class.java]
    }

    private fun clickOnPoster() {
        adapter.onClickPoster = {
            val bundle = Bundle()
            bundle.putInt("id", it.id)
            findNavController().navigate(R.id.detail_fragment, bundle)
        }
    }

    private fun getColumnCount(): Int {
        val displayMetrics = resources.displayMetrics
        val fl = (displayMetrics.widthPixels / displayMetrics.density).toInt()
        return if (fl / 185 > 3) fl / 185 else 3
    }

    private fun getMoviesFromDb() {
        ratedViewModel?.allMovies?.observe(viewLifecycleOwner, {
            if (page == 1) {
                this.adapter.listMovies = it as MutableList<Movie>
            }
        })
    }

    private fun reachEndListener() {
        adapter.onReachEndListener = {
            if (!isLoading) {
                isLoading = true
                getMovies()
                binding.progressBarLoading.visibility = View.VISIBLE
            }
        }
    }

    private fun getMovies() {
        ratedViewModel?.getTopRatedMovies(lang, page)?.observe(viewLifecycleOwner, {
            if (ratedViewModel?.cacheListMovie?.size == 0 && !adapter.listMovies.containsAll(it)) {
                clearMoviesFromAll()
                it.forEach { movie -> ratedViewModel!!.addMovie(movie) }
                adapter.listMovies = it as MutableList<Movie>
                page++
            }
            isLoading = false
            lifecycleScope.launch(Dispatchers.Main) {
                binding.progressBarLoading.visibility = View.INVISIBLE
            }
        })
    }

    private fun getException() {
        ratedViewModel?.exception?.observe(viewLifecycleOwner, {
            getMoviesFromDb()
            Toast.makeText(
                this.context,
                getString(R.string.no_internet_connection),
                Toast.LENGTH_LONG
            ).show()
        })
    }

    private fun clearMoviesFromAll() {
        if (page == 1) {
            adapter.clear()
            ratedViewModel?.removeAllMovies()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}