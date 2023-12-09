package com.lans.task3_pokedex.presentation.pokemon

import com.lans.task3_pokedex.domain.model.PokedexEntry

data class PokemonUIState(
    var data: List<PokedexEntry> = emptyList(),
    var favorite: List<String> = emptyList(),
    var error: String = "",
    var isLoading: Boolean = false,
)
