package com.lans.task3_pokedex.data.source.remote.dto

import com.lans.task3_pokedex.domain.model.PokedexEntry
import com.squareup.moshi.Json

data class PokemonDetailDto(
    @field:Json(name = "id")
    val id: Int,
    @field:Json(name = "name")
    val name: String,
    @field:Json(name = "sprites")
    val pokemonSprites: PokemonSprites,
    @field:Json(name = "types")
    val types: List<Types>,
)

fun PokemonDetailDto.toDomain() = PokedexEntry(
    number = id,
    pokemonName = name,
    imageUrl = pokemonSprites.frontDefault,
    types = types.map { it.type.name }
)
