package com.lans.task3_pokedex.domain.model

import com.lans.task3_pokedex.data.source.local.entity.UserEntity

data class User(
    val id: Int = 0,
    val username: String = "",
    val password: String = "",
)

fun User.toEntity() = UserEntity(
    id = id,
    username = username,
    password = password
)