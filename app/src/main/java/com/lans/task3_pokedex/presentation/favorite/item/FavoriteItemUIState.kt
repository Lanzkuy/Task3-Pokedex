package com.lans.task3_pokedex.presentation.favorite.item

import com.lans.task3_pokedex.domain.model.Item

data class FavoriteItemUIState(
    var data: List<Item> = emptyList(),
    var error: String = "",
    var isLoading: Boolean = false,
)
