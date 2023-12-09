package com.lans.task3_pokedex.presentation.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.lans.task3_pokedex.R
import com.lans.task3_pokedex.databinding.ItemItemBinding
import com.lans.task3_pokedex.domain.model.Item

class ItemAdapter(private var list: List<Item>, val onClick: ((String) -> Unit)? = null) :
    RecyclerView.Adapter<ItemAdapter.ViewHolder>() {
    inner class ViewHolder(val binding: ItemItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val (pokemonName, imageUrl, isLiked) = list[position]
        holder.binding.tvName.text = pokemonName.replaceFirstChar(Char::titlecase)
        holder.binding.ivItem.load(imageUrl)

        if (onClick == null) {
            holder.binding.ivFavorite.visibility = View.GONE
        }

        holder.binding.ivFavorite.setOnClickListener {
            val animation = AnimationUtils.loadAnimation(holder.itemView.context, R.anim.scale_down)
            animation.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation) {}

                override fun onAnimationEnd(animation: Animation) {
                    onClick?.let { listener -> listener(pokemonName) }
                }

                override fun onAnimationRepeat(animation: Animation) {}
            })

            holder.binding.ivFavorite.startAnimation(animation)
        }

        if (isLiked) {
            holder.binding.ivFavorite.setColorFilter(
                ContextCompat.getColor(
                    holder.itemView.context,
                    R.color.primary
                )
            )
        } else {
            holder.binding.ivFavorite.setColorFilter(
                ContextCompat.getColor(
                    holder.itemView.context,
                    R.color.light_gray
                )
            )
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun filter(filterList: List<Item>) {
        list = filterList
        notifyDataSetChanged()
    }
}