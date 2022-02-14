package com.andriukhov.mymovies.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.andriukhov.mymovies.R
import com.andriukhov.mymovies.domain.pojo.Image
import com.squareup.picasso.Picasso

class ImagesRecycleViewAdapter: RecyclerView.Adapter<ImagesRecycleViewAdapter.ImagesViewHolder>() {

    var images = mutableListOf<Image>()
    set(value) {
        field.clear()
        field.addAll(value)
        notifyDataSetChanged()
    }

    inner class ImagesViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var imageViewImage: ImageView? = null

        init {
            imageViewImage = view.findViewById(R.id.imageViewImage)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImagesViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.backdropimg_item, parent, false)
        return ImagesViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ImagesViewHolder, position: Int) {
        val image = images[position]
        Picasso.get().load(image.getFullImagePath()).into(holder.imageViewImage)
    }

    override fun getItemCount(): Int = images.size
}