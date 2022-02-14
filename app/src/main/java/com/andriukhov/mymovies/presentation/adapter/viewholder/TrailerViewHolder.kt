package com.andriukhov.mymovies.presentation.adapter.viewholder

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.andriukhov.mymovies.R

class TrailerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var textViewTrailer: TextView? = null

    init {
        textViewTrailer = itemView.findViewById(R.id.textViewTrailer)
    }
}