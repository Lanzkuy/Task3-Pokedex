package com.lans.task3_pokedex.domain.repository

import com.lans.task3_pokedex.common.Resource
import com.lans.task3_pokedex.domain.model.Item
import kotlinx.coroutines.flow.Flow

interface IItemRepository {
    suspend fun getAllItem(): Flow<Resource<List<Item>>>
}