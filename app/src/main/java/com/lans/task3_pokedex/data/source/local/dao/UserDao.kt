package com.lans.task3_pokedex.data.source.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Upsert
import com.lans.task3_pokedex.data.source.local.entity.UserEntity

@Dao
interface UserDao {
    @Insert
    fun create(userEntity: UserEntity)

    @Query("SELECT * FROM UserEntity WHERE username == :username")
    fun getUserByUsername(username: String): UserEntity?
}