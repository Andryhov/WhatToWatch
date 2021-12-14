package com.example.mymovies

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.mymovies.adapter.MoviesRecyclerViewAdapter
import com.example.mymovies.databinding.ActivityMainBinding
import com.example.mymovies.db.Movie
import com.example.mymovies.listener.OnReachEndListener
import com.example.mymovies.listener.OnStartLoadingListener
import com.example.mymovies.listener.PosterClickListener
import com.example.mymovies.utils.NetworkUtils
import com.example.mymovies.viewModels.MoviesManagerViewModel
import com.example.mymovies.viewModels.MovieViewModel
import com.example.mymovies.viewModels.MovieViewModelFactory
import com.example.mymovies.viewModels.NetworkViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private var page = 1
    private var isChecked = false
    private var isLoading = false
    private var adapter = MoviesRecyclerViewAdapter()

    companion object {
        private lateinit var lang: String
    }

    private val networkViewModel: MoviesManagerViewModel by viewModels {
        NetworkViewModelFactory()
    }

    private val moviesViewModel: MovieViewModel by viewModels {
        MovieViewModelFactory((application as MoviesApplication).moviesRepository)
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

    private fun getColumnCount(): Int {
        val displayMetrics = resources.displayMetrics
        val fl = (displayMetrics.widthPixels / displayMetrics.density).toInt()
        return if (fl / 185 > 2) fl / 185 else 2
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        lang = Locale.getDefault().language

        val recyclerView = binding.recyclerViewPosters
        adapter = MoviesRecyclerViewAdapter()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = GridLayoutManager(this@MainActivity, getColumnCount())
        observeNetworkView()
        reachEndListener()
        clickListenerSwitch()
        popularityTextClickListener(page)
        ratedTextViewClickListener(page)
        loadDataFromNetwork(page, isChecked)
        clickOnPoster()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("page", page)
        moviesViewModel.cacheListMovie.addAll(adapter.listMovies)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        page = savedInstanceState.getInt("page")
        adapter.listMovies.addAll(moviesViewModel.cacheListMovie)
        moviesViewModel.cacheListMovie.clear()
    }

    override fun onResume() {
        super.onResume()
        isLoading = false
        moviesViewModel.cacheListMovie.clear()
    }

    private fun reachEndListener() {
        adapter.onReachEndListener = object : OnReachEndListener {
            override fun onReachEnd() {
                if (!isLoading) {
                    loadDataFromNetwork(page, isChecked)
                }
            }
        }
    }

    private fun clickOnPoster() {
        adapter.posterClickListener = object : PosterClickListener {
            override fun onPosterClickListener(position: Int) {
                val movie = adapter.listMovies[position]
                val intent = Intent(this@MainActivity, DetailActivity::class.java)
                intent.putExtra("id", movie.id)
                startActivity(intent)
            }
        }
    }

    private fun ratedTextViewClickListener(page: Int) {
        binding.textViewTopRated.setOnClickListener {
            loadDataFromNetwork(page, true)
            binding.switchSort.isChecked = true
        }
    }

    private fun popularityTextClickListener(page: Int) {
        binding.textViewPopularity.setOnClickListener {
            loadDataFromNetwork(page, false)
            binding.switchSort.isChecked = false
        }
    }

    private fun loadDataFromNetwork(page: Int, switch: Boolean) {
        if (moviesViewModel.cacheListMovie.size == 0) {
            networkViewModel.loadMoviesList(NetworkUtils.buildURL(page, switch, lang))
        }
        NetworkUtils.onStartLoadingListener = object : OnStartLoadingListener {
            override fun onStartLoading() {
                isLoading = true
            }
        }
        binding.progressBarLoading.visibility = View.VISIBLE
    }

    private fun clickListenerSwitch() {
        binding.switchSort.setOnCheckedChangeListener { _, isChecked ->
            this.isChecked = isChecked
            page = 1
            loadDataFromNetwork(page, isChecked)
            if (!isChecked) {
                binding.textViewPopularity.setTextColor(resources.getColor(R.color.pink))
                binding.textViewTopRated.setTextColor(resources.getColor(R.color.white))
            } else {
                binding.textViewTopRated.setTextColor(resources.getColor(R.color.pink))
                binding.textViewPopularity.setTextColor(resources.getColor(R.color.white))
            }
        }
    }

    private fun observeMovies() {
        moviesViewModel.allMovies.observe(this, {
            if (page == 1) {
                this.adapter.listMovies = it as MutableList<Movie>
            }
        })
    }

    private fun observeNetworkView() {
        networkViewModel.listMovies.observe(this, {
            if (it.isNotEmpty()) {
                if (moviesViewModel.cacheListMovie.size == 0) {
                    if (page == 1) {
                        adapter.clear()
                        moviesViewModel.removeAllMovies()
                    }
                    it.forEach { movie -> moviesViewModel.addMovie(movie) }
                    adapter.listMovies = it as MutableList<Movie>
                    page++
                }
            } else {
                observeMovies()
                Toast.makeText(
                    this,
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
}