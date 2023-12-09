package com.lans.task3_pokedex.presentation.item

import com.lans.task3_pokedex.domain.model.Item

data class ItemUIState(
    var data: List<Item> = emptyList(),
    var favorite: List<String> = emptyList(),
    var error: String = "",
    var isLoading: Boolean = false,
)
