package com.lans.task3_pokedex.data.repository

import com.lans.task3_pokedex.common.Resource
import com.lans.task3_pokedex.data.source.remote.SafeApiCall
import com.lans.task3_pokedex.data.source.remote.api.PokeApi
import com.lans.task3_pokedex.data.source.remote.dto.toDomain
import com.lans.task3_pokedex.domain.model.Item
import com.lans.task3_pokedex.domain.repository.IItemRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class ItemRepository @Inject constructor(
    private val api: PokeApi,
) : IItemRepository, SafeApiCall {
    override suspend fun getAllItem(): Flow<Resource<List<Item>>> {
        return flow {
            emit(safeCall {
                api.getAllItem().results.map { pokemon ->
                    api.getItemDetail(pokemon.name).toDomain()
                }
            })
        }.flowOn(Dispatchers.IO)
    }
}