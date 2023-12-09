package com.lans.task3_pokedex.domain.repository

import com.lans.task3_pokedex.common.Resource
import com.lans.task3_pokedex.domain.model.User
import kotlinx.coroutines.flow.Flow

interface IUserRepository {
    suspend fun getSession(): Flow<Resource<Int>>
    suspend fun saveSession(userId: Int)
    suspend fun clear()
}