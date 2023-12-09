package com.lans.task3_pokedex.presentation.favorite.pokemon

import com.lans.task3_pokedex.domain.model.PokedexEntry

data class FavoritePokemonUIState(
    var data: List<PokedexEntry> = emptyList(),
    var error: String = "",
    var isLoading: Boolean = false,
)
