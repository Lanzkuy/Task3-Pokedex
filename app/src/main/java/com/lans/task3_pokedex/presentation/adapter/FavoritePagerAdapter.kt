package com.lans.task3_pokedex.presentation.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.lans.task3_pokedex.presentation.favorite.item.FavoriteItemFragment
import com.lans.task3_pokedex.presentation.favorite.pokemon.FavoritePokemonFragment

class FavoritePagerAdapter(fm: FragmentManager, private val fragmentCount: Int) :
    FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private val fragmentTitleList = mutableListOf("Pokemon", "Item")

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> FavoritePokemonFragment()
            1 -> FavoriteItemFragment()
            else -> FavoritePokemonFragment()
        }
    }

    override fun getPageTitle(position: Int): CharSequence {
        return fragmentTitleList[position]
    }

    override fun getCount(): Int = fragmentCount
}