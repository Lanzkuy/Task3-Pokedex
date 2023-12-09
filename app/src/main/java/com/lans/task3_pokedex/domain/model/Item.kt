package com.lans.task3_pokedex.domain.model

data class Item(
    val itemName: String,
    val imageUrl: String,
    var isLiked: Boolean = false,
)
