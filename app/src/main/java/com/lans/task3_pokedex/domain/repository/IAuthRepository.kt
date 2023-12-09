package com.lans.task3_pokedex.domain.repository

import com.lans.task3_pokedex.common.Resource
import com.lans.task3_pokedex.domain.model.User
import kotlinx.coroutines.flow.Flow

interface IAuthRepository {
    suspend fun signIn(username: String, password: String): Flow<Resource<User>>
    suspend fun signUp(user: User): Flow<Resource<Boolean>>
}