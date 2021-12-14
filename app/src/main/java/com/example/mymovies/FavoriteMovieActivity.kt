package com.example.mymovies

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.example.mymovies.adapter.MoviesRecyclerViewAdapter
import com.example.mymovies.databinding.ActivityFavoriteMovieBinding
import com.example.mymovies.db.Movie
import com.example.mymovies.listener.PosterClickListener
import com.example.mymovies.viewModels.MovieViewModel
import com.example.mymovies.viewModels.MovieViewModelFactory

class FavoriteMovieActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoriteMovieBinding

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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteMovieBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val recyclerViewFavorite = binding.recyclerViewFavorite
        val adapter = MoviesRecyclerViewAdapter()
        recyclerViewFavorite.adapter = adapter
        recyclerViewFavorite.layoutManager = GridLayoutManager(this, 2)

        loadFavoriteMovie(adapter)
        clickOnPoster(adapter)
    }

    private fun clickOnPoster(adapter: MoviesRecyclerViewAdapter) {
        adapter.posterClickListener = object : PosterClickListener {
            override fun onPosterClickListener(position: Int) {
                val movie = adapter.listMovies[position]
                val intent = Intent(this@FavoriteMovieActivity, DetailActivity::class.java)
                intent.putExtra("id", movie.id)
                intent.putExtra("from", "favorite")
                startActivity(intent)
            }
        }
    }

    private fun loadFavoriteMovie(adapter: MoviesRecyclerViewAdapter) {
        moviesViewModel.allFavouriteMovies.observe(this, {
            adapter.clear()
            if (it != null) {
                val listMovies = mutableListOf<Movie>()
                listMovies.addAll(it.map { favorite -> Movie(favorite) })
                adapter.listMovies = listMovies
            }
        })
    }
}