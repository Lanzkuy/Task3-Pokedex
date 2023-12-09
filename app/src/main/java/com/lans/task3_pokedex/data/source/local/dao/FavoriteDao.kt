package com.lans.task3_pokedex.data.source.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Upsert
import com.lans.task3_pokedex.data.source.local.entity.FavoriteEntity
import com.lans.task3_pokedex.data.source.local.entity.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteDao {
    @Insert
    fun create(favoriteEntity: FavoriteEntity)

    @Query("SELECT * FROM FavoriteEntity WHERE type == 'pokemon' AND user_id == :userId")
    fun getAllFavoritePokemon(userId: String): Flow<List<FavoriteEntity>?>

    @Query("SELECT * FROM FavoriteEntity WHERE type == 'item' AND user_id == :userId")
    fun getAllFavoriteItem(userId: String): Flow<List<FavoriteEntity>?>

    @Query("DELETE FROM FavoriteEntity WHERE name = :name")
    fun delete(name: String)
}