package com.andriukhov.mymovies.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.andriukhov.mymovies.R
import com.andriukhov.mymovies.adapter.viewholder.ActorViewHolder
import com.andriukhov.mymovies.domain.pojo.Actor
import com.squareup.picasso.Picasso

class ActorsRecyclerViewAdapter :
    RecyclerView.Adapter<ActorViewHolder>() {

    var onActorClick: ((Actor) -> Unit)? = null

    var actors = mutableListOf<Actor>()
        set(value) {
            field.clear()
            field.addAll(value)
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActorViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.actor_item, parent, false)
        return ActorViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ActorViewHolder, position: Int) {
        val actor = actors[position]
        Picasso.get().load(actor.getFullProfilePhotoPath()).placeholder(R.drawable.placeholder).into(holder.imageViewActorPhoto)
        holder.textViewActorName?.text = actor.name
        holder.textViewCharacterName?.text = String.format("(%s)", actor.character)

        holder.itemView.setOnClickListener {
            onActorClick?.invoke(actor)
        }
    }

    override fun getItemCount(): Int = actors.size
}