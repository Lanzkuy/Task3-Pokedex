package com.lans.task3_pokedex.data.source.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.lans.task3_pokedex.domain.model.User

@Entity(indices = [Index(value = ["username"], unique = true)])
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    @ColumnInfo(name = "username")
    val username: String,
    @ColumnInfo(name = "password")
    val password: String,
)

fun UserEntity.toDomain() = User(
    id = id,
    username = username,
    password = password
)
