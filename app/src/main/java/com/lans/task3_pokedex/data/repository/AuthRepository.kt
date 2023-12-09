package com.lans.task3_pokedex.data.repository

import android.database.sqlite.SQLiteConstraintException
import com.lans.task3_pokedex.common.Resource
import com.lans.task3_pokedex.data.source.local.dao.UserDao
import com.lans.task3_pokedex.data.source.local.entity.toDomain
import com.lans.task3_pokedex.domain.model.User
import com.lans.task3_pokedex.domain.model.toEntity
import com.lans.task3_pokedex.domain.repository.IAuthRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val userDao: UserDao,
) : IAuthRepository {
    override suspend fun signIn(username: String, password: String): Flow<Resource<User>> {
        return flow {
            val user = userDao.getUserByUsername(username)
            if (user != null && user.password == password) {
                emit(Resource.Success(user.toDomain()))
            } else {
                emit(Resource.Error("Username or password was wrong"))
            }
        }.catch { e ->
            emit(Resource.Error(e.message.toString()))
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun signUp(user: User): Flow<Resource<Boolean>> {
        return flow<Resource<Boolean>> {
            userDao.create(user.toEntity())
            emit(Resource.Success(true))
        }.catch { e ->
            when (e) {
                is SQLiteConstraintException -> {
                    emit(Resource.Error("Username already exist"))
                }

                else -> {
                    emit(Resource.Error("Something went wrong"))
                }
            }
        }.flowOn(Dispatchers.IO)
    }
}