package com.lans.task3_pokedex.presentation.adapter

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.lans.task3_pokedex.R
import com.lans.task3_pokedex.databinding.ItemPokemonBinding
import com.lans.task3_pokedex.domain.model.PokedexEntry

@SuppressLint("NotifyDataSetChanged")
class PokemonAdapter(
    private var list: List<PokedexEntry>,
    val onClick: ((String) -> Unit)? = null,
) :
    RecyclerView.Adapter<PokemonAdapter.ViewHolder>() {
    inner class ViewHolder(val binding: ItemPokemonBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemPokemonBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    @SuppressLint("SetTextI18n", "Recycle")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val (number, pokemonName, imageUrl, types, isLiked) = list[position]
        holder.binding.tvName.text = pokemonName.replaceFirstChar(Char::titlecase)
        holder.binding.tvNumber.text = "#${number.toString().padStart(3, '0')}"
        holder.binding.ivPokemon.load(imageUrl)
        holder.binding.chipType1.text = types[0]

        if (types.count() > 1) {
            holder.binding.chipType2.text = types[1]
        } else {
            holder.binding.chipType2.visibility = View.GONE
        }

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

    fun filter(filterList: List<PokedexEntry>) {
        list = filterList
        notifyDataSetChanged()
    }
}