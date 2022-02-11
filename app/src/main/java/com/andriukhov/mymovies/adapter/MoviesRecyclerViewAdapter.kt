package com.andriukhov.mymovies.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.andriukhov.mymovies.R
import com.andriukhov.mymovies.adapter.viewholder.MoviesViewHolder
import com.andriukhov.mymovies.pojo.Movie
import com.squareup.picasso.Picasso

class MoviesRecyclerViewAdapter : RecyclerView.Adapter<MoviesViewHolder>() {
    var onClickPoster: ((Movie) -> Unit)? = null
    var onReachEndListener: (() -> Unit)? = null

    var listMovies = mutableListOf<Movie>()
        set(value) {
            listMovies.addAll(value)
            notifyDataSetChanged()
        }

    fun clear() {
        listMovies.clear()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MoviesViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.movie_item, parent, false)
        return MoviesViewHolder(itemView)
    }

    override fun onBindViewHolder(
        holder: MoviesViewHolder,
        position: Int
    ) {
        if (listMovies.size >= 20 && position >= listMovies.size - 1) {
            onReachEndListener?.invoke()
        }
        val movie = listMovies[position]
        Picasso.get().load(movie.getFullSmallPosterPath())
            .placeholder(R.drawable.placeholder).into(holder.imageViewPoster)
        holder.textViewRaiting?.text = movie.voteAverage.toString()
        holder.textViewYear?.text = movie.releaseDate.substring(0, 4)
        holder.itemView.setOnClickListener {
            onClickPoster?.invoke(movie)
        }
    }

    override fun getItemCount(): Int = listMovies.size
}