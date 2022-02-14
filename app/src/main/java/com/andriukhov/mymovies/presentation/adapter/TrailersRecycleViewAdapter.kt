package com.andriukhov.mymovies.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.andriukhov.mymovies.R
import com.andriukhov.mymovies.presentation.adapter.viewholder.TrailerViewHolder
import com.andriukhov.mymovies.domain.pojo.Trailer

class TrailersRecycleViewAdapter : RecyclerView.Adapter<TrailerViewHolder>() {

    var onClickTrailer: ((Trailer) -> Unit)? = null

    var trailersList = mutableListOf<Trailer>()
        set(value) {
            field.clear()
            field.addAll(value)
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrailerViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.trailer_item, parent, false)
        return TrailerViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: TrailerViewHolder, position: Int) {
        val trailer = trailersList[position]
        holder.textViewTrailer?.text = trailer.name
        holder.itemView.setOnClickListener {
            onClickTrailer?.invoke(trailer)
        }
    }

    override fun getItemCount(): Int = trailersList.size
}