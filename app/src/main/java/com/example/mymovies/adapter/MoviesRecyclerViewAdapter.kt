package com.example.mymovies.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.mymovies.R
import com.example.mymovies.listener.PosterClickListener
import com.example.mymovies.db.Movie
import com.example.mymovies.listener.OnReachEndListener
import com.squareup.picasso.Picasso

class MoviesRecyclerViewAdapter :
    RecyclerView.Adapter<MoviesRecyclerViewAdapter.MoviesViewHolder>() {

    lateinit var posterClickListener: PosterClickListener
    lateinit var onReachEndListener: OnReachEndListener

    var listMovies = mutableListOf<Movie>()
        set(value) {
            listMovies.addAll(value)
            notifyDataSetChanged()
        }

    inner class MoviesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imageViewPoster: ImageView? = null

        init {
            imageViewPoster = itemView.findViewById(R.id.imageViewSmallPoster)
            itemView.setOnClickListener{
                posterClickListener.onPosterClickListener(adapterPosition)
            }
        }
    }

    fun clear() {
        listMovies.clear()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoviesViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.movie_item, parent, false)
        return MoviesViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MoviesViewHolder, position: Int) {
        if(listMovies.size >= 20 && position >= listMovies.size - 1) {
            onReachEndListener.onReachEnd()
        }
        val movie = listMovies[position]
        Picasso.get().load(movie.posterPath).placeholder(R.drawable.placeholder).into(holder.imageViewPoster)
    }

    override fun getItemCount(): Int = listMovies.size
}