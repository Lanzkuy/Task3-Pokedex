package com.lans.task3_pokedex.domain.model

data class PokedexEntry(
    val number: Int,
    val pokemonName: String,
    val imageUrl: String,
    val types: List<String>,
    var isLiked: Boolean = false,
)
