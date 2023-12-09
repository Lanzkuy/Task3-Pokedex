package com.lans.task3_pokedex.data.repository

import android.database.sqlite.SQLiteConstraintException
import com.lans.task3_pokedex.common.Resource
import com.lans.task3_pokedex.data.source.local.dao.FavoriteDao
import com.lans.task3_pokedex.data.source.local.entity.FavoriteEntity
import com.lans.task3_pokedex.data.source.remote.SafeApiCall
import com.lans.task3_pokedex.data.source.remote.api.PokeApi
import com.lans.task3_pokedex.data.source.remote.dto.toDomain
import com.lans.task3_pokedex.domain.model.Item
import com.lans.task3_pokedex.domain.model.PokedexEntry
import com.lans.task3_pokedex.domain.repository.IFavoriteRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class FavoriteRepository @Inject constructor(
    private val favoriteDao: FavoriteDao,
    private val api: PokeApi,
) : IFavoriteRepository, SafeApiCall {
    override suspend fun create(userId: String, type: Int, name: String) {
        try {
            val entityType = when (type) {
                1 -> "pokemon"
                2 -> "item"
                else -> throw IllegalArgumentException("Invalid type: $type")
            }
            favoriteDao.create(FavoriteEntity(0, userId, entityType, name))
        } catch (exception: SQLiteConstraintException) {
            favoriteDao.delete(name)
        }
    }

    override suspend fun getAllFavoritePokemon(userId: String): Flow<Resource<List<String>>> {
        return flow {
            favoriteDao.getAllFavoritePokemon(userId).collect { result ->
                if (result != null) {
                    emit(Resource.Success(result.map { it.name }))
                }
            }
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun getAllFavoriteItem(userId: String): Flow<Resource<List<String>>> {
        return flow {
            favoriteDao.getAllFavoriteItem(userId).collect { result ->
                if (result != null) {
                    emit(Resource.Success(result.map { it.name }))
                }
            }
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun getAllFavoritePokemonDetail(userId: String): Flow<Resource<List<PokedexEntry>>> {
        return flow {
            val result = favoriteDao.getAllFavoritePokemon(userId).first()
            if (result != null) {
                emit(safeCall {
                    result.map { pokemon ->
                        api.getPokemonDetail(pokemon.name).toDomain().copy(isLiked = true)
                    }
                })
            }
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun getAllFavoriteItemDetail(userId: String): Flow<Resource<List<Item>>> {
        return flow {
            val result = favoriteDao.getAllFavoriteItem(userId).first()
            if (result != null) {
                emit(safeCall {
                    result.map { item ->
                        api.getItemDetail(item.name).toDomain().copy(isLiked = true)
                    }
                })
            }
        }.flowOn(Dispatchers.IO)
    }
}