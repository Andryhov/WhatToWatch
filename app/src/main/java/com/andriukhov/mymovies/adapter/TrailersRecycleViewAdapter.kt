package com.andriukhov.mymovies.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.andriukhov.mymovies.R
import com.andriukhov.mymovies.data.Trailer
import com.andriukhov.mymovies.listener.TrailerClickListener

class TrailersRecycleViewAdapter :
    RecyclerView.Adapter<TrailersRecycleViewAdapter.TrailerViewHolder>() {

    lateinit var trailerClickListener: TrailerClickListener

    var trailersList = mutableListOf<Trailer>()
        set(value) {
            field.clear()
            field.addAll(value)
            notifyDataSetChanged()
        }

    inner class TrailerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var textViewTrailer: TextView? = null

        init {
            textViewTrailer = itemView.findViewById(R.id.textViewTrailer)
            itemView.setOnClickListener {
                trailerClickListener.onTrailerClickListener(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrailerViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.trailer_item, parent, false)
        return TrailerViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: TrailerViewHolder, position: Int) {
        val trailer = trailersList[position]
        holder.textViewTrailer?.text = trailer.name
    }

    override fun getItemCount(): Int = trailersList.size
}