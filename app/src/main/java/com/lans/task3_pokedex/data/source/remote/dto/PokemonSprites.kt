package com.lans.task3_pokedex.data.source.remote.dto

import com.squareup.moshi.Json

data class PokemonSprites(
    @field:Json(name = "front_default")
    val frontDefault: String,
)
