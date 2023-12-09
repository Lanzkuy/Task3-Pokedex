package com.lans.task3_pokedex.data.source.remote.dto

import com.squareup.moshi.Json

data class ItemSprites(
    @field:Json(name = "default")
    val default: String,
)
