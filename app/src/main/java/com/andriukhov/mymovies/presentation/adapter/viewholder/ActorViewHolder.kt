package com.andriukhov.mymovies.presentation.adapter.viewholder

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.andriukhov.mymovies.R

class ActorViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    var imageViewActorPhoto: ImageView? = null
    var textViewActorName: TextView? = null
    var textViewCharacterName: TextView? = null

    init {
        imageViewActorPhoto = view.findViewById(R.id.imageViewActorPhoto)
        textViewActorName = view.findViewById(R.id.textViewActorName)
        textViewCharacterName = view.findViewById(R.id.textViewCharacterName)
    }
}