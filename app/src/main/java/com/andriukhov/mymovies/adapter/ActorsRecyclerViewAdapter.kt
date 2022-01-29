package com.andriukhov.mymovies.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.andriukhov.mymovies.R
import com.andriukhov.mymovies.pojo.Actor
import com.andriukhov.mymovies.listener.ActorClickListener
import com.squareup.picasso.Picasso

class ActorsRecyclerViewAdapter :
    RecyclerView.Adapter<ActorsRecyclerViewAdapter.ActorsViewHolder>() {

    lateinit var actorClickListener: ActorClickListener

    var actors = mutableListOf<Actor>()
        set(value) {
            field.clear()
            field.addAll(value)
            notifyDataSetChanged()
        }

    inner class ActorsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var imageViewActorPhoto: ImageView? = null
        var textViewActorName: TextView? = null
        var textViewCharacterName: TextView? = null

        init {
            imageViewActorPhoto = view.findViewById(R.id.imageViewActorPhoto)
            textViewActorName = view.findViewById(R.id.textViewActorName)
            textViewCharacterName = view.findViewById(R.id.textViewCharacterName)

            view.setOnClickListener {
                actorClickListener.onActorClickListener(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActorsViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.actor_item, parent, false)
        return ActorsViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ActorsViewHolder, position: Int) {
        val actor = actors[position]
        Picasso.get().load(actor.getFullProfilePhotoPath()).placeholder(R.drawable.placeholder).into(holder.imageViewActorPhoto)
        holder.textViewActorName?.text = actor.name
        holder.textViewCharacterName?.text = String.format("(%s)", actor.character)
    }

    override fun getItemCount(): Int = actors.size
}