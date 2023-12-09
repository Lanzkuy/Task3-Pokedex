package com.lans.task3_pokedex.domain.repository

import com.lans.task3_pokedex.common.Resource
import com.lans.task3_pokedex.domain.model.PokedexEntry
import kotlinx.coroutines.flow.Flow

interface IPokemonRepository {
    suspend fun getAllPokemon(): Flow<Resource<List<PokedexEntry>>>
}