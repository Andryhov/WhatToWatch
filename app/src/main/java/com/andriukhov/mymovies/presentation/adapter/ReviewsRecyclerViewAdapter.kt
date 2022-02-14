package com.andriukhov.mymovies.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.andriukhov.mymovies.R
import com.andriukhov.mymovies.domain.pojo.Review

class ReviewsRecyclerViewAdapter: RecyclerView.Adapter<ReviewsRecyclerViewAdapter.ReviewsViewHolder>() {

    var reviewsList = mutableListOf<Review>()
    set(value) {
        field.clear()
        field.addAll(value)
        notifyDataSetChanged()
    }

    inner class ReviewsViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        var textViewAuthor: TextView? = null
        var textViewContent: TextView? = null

        init {
            textViewAuthor = itemView.findViewById(R.id.textViewAuthor)
            textViewContent = itemView.findViewById(R.id.textViewContent)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewsViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.review_item, parent, false)
        return ReviewsViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ReviewsViewHolder, position: Int) {
        val review = reviewsList[position]
        holder.textViewAuthor?.text = review.author
        holder.textViewContent?.text = review.content
    }

    override fun getItemCount(): Int = reviewsList.size
}