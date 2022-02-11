package com.andriukhov.mymovies.adapter.viewholder

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.andriukhov.mymovies.R

class MoviesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var imageViewPoster: ImageView? = null
    var textViewYear: TextView? = null
    var textViewRaiting: TextView? = null

    init {
        imageViewPoster = itemView.findViewById(R.id.imageViewSmallPoster)
        textViewRaiting = itemView.findViewById(R.id.textViewRaiting)
        textViewYear = itemView.findViewById(R.id.texViewYear)
    }
}