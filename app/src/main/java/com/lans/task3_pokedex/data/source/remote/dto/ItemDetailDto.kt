package com.lans.task3_pokedex.data.source.remote.dto

import com.lans.task3_pokedex.domain.model.Item
import com.squareup.moshi.Json

data class ItemDetailDto(
    @field:Json(name = "name")
    val name: String,
    @field:Json(name = "sprites")
    val sprites: ItemSprites,
)

fun ItemDetailDto.toDomain() = Item(
    itemName = name,
    imageUrl = sprites.default
)