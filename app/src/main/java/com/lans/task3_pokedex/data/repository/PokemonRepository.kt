package com.lans.task3_pokedex.data.repository

import com.lans.task3_pokedex.common.Resource
import com.lans.task3_pokedex.data.source.remote.SafeApiCall
import com.lans.task3_pokedex.data.source.remote.api.PokeApi
import com.lans.task3_pokedex.data.source.remote.dto.toDomain
import com.lans.task3_pokedex.domain.model.PokedexEntry
import com.lans.task3_pokedex.domain.repository.IPokemonRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class PokemonRepository @Inject constructor(
    private val api: PokeApi,
) : IPokemonRepository, SafeApiCall {
    override suspend fun getAllPokemon(): Flow<Resource<List<PokedexEntry>>> {
        return flow {
            emit(safeCall {
                api.getAllPokemon().results.map { pokemon ->
                    api.getPokemonDetail(pokemon.name).toDomain()
                }
            })
        }.flowOn(Dispatchers.IO)
    }
}