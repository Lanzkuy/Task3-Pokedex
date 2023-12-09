package com.lans.task3_pokedex.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.lans.task3_pokedex.data.source.local.dao.FavoriteDao
import com.lans.task3_pokedex.data.source.local.dao.UserDao
import com.lans.task3_pokedex.data.source.local.entity.FavoriteEntity
import com.lans.task3_pokedex.data.source.local.entity.UserEntity

@Database(
    entities = [UserEntity::class,
        FavoriteEntity::class],
    version = 3
)
abstract class AppDatabase : RoomDatabase() {
    abstract val userDao: UserDao
    abstract val favoriteDao: FavoriteDao
}