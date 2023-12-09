package com.lans.task3_pokedex.data.repository

import com.lans.task3_pokedex.common.Resource
import com.lans.task3_pokedex.data.source.local.DataStoreManager
import com.lans.task3_pokedex.domain.repository.IUserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class UserRepository(
    private val dataStoreManager: DataStoreManager,
) : IUserRepository {
    override suspend fun getSession(): Flow<Resource<Int>> {
        return flow {
            dataStoreManager.userId.collect { userId ->
                emit(Resource.Success(userId))
            }
        }
    }

    override suspend fun saveSession(userId: Int) {
        dataStoreManager.storeData(DataStoreManager.USER_ID, userId)
    }

    override suspend fun clear() {
        dataStoreManager.clear()
    }
}