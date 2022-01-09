package com.andriukhov.mymovies.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.andriukhov.mymovies.R

class GenreRecyclerViewAdapter : RecyclerView.Adapter<GenreRecyclerViewAdapter.GenresViewHolder>() {

    var genreList = mutableListOf<String>()
        set(value) {
            field.clear()
            field.addAll(value)
            notifyDataSetChanged()
        }

    inner class GenresViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var textViewGenre: TextView? = null

        init {
            textViewGenre = itemView.findViewById(R.id.textViewGenre)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenresViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.genre_item, parent, false)
        return GenresViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: GenresViewHolder, position: Int) {
        val genre = genreList[position]
        holder.textViewGenre?.text = genre
    }

    override fun getItemCount(): Int = genreList.size
}