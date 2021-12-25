package com.andriukhov.mymovies.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.andriukhov.mymovies.R
import com.andriukhov.mymovies.data.Movie
import com.andriukhov.mymovies.listener.OnReachEndListener
import com.andriukhov.mymovies.listener.PosterClickListener
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
        var textViewYear: TextView? = null
        var textViewRaiting: TextView? = null

        init {
            imageViewPoster = itemView.findViewById(R.id.imageViewSmallPoster)
            textViewRaiting = itemView.findViewById(R.id.textViewRaiting)
            textViewYear = itemView.findViewById(R.id.texViewYear)
            itemView.setOnClickListener{
                posterClickListener.onPosterClickListener(adapterPosition)
            }
        }
    }

    fun clear() {
        listMovies.clear()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoviesRecyclerViewAdapter.MoviesViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.movie_item, parent, false)
        return MoviesViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MoviesRecyclerViewAdapter.MoviesViewHolder, position: Int) {
        if(listMovies.size >= 20 && position >= listMovies.size - 1) {
            onReachEndListener.onReachEnd()
        }
        val movie = listMovies[position]
        Picasso.get().load(movie.posterPath).placeholder(R.drawable.placeholder).into(holder.imageViewPoster)
        holder.textViewRaiting?.text = movie.voteAverage.toString()
        holder.textViewYear?.text = movie.releaseDate.substring(0,4)
    }

    override fun getItemCount(): Int = listMovies.size
}