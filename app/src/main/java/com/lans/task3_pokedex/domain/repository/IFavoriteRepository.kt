package com.lans.task3_pokedex.domain.repository

import com.lans.task3_pokedex.common.Resource
import com.lans.task3_pokedex.domain.model.Item
import com.lans.task3_pokedex.domain.model.PokedexEntry
import kotlinx.coroutines.flow.Flow

interface IFavoriteRepository {
    suspend fun create(userId: String, type: Int, name: String)
    suspend fun getAllFavoritePokemon(userId: String): Flow<Resource<List<String>>>
    suspend fun getAllFavoriteItem(userId: String): Flow<Resource<List<String>>>
    suspend fun getAllFavoritePokemonDetail(userId: String): Flow<Resource<List<PokedexEntry>>>
    suspend fun getAllFavoriteItemDetail(userId: String): Flow<Resource<List<Item>>>
}