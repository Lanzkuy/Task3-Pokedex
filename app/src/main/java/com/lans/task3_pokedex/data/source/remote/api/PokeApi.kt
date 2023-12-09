package com.lans.task3_pokedex.data.source.remote.api

import com.lans.task3_pokedex.data.source.remote.dto.ItemDetailDto
import com.lans.task3_pokedex.data.source.remote.dto.ItemDto
import com.lans.task3_pokedex.data.source.remote.dto.PokemonDetailDto
import com.lans.task3_pokedex.data.source.remote.dto.PokemonDto
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface PokeApi {
    @GET("pokemon?offset=0&limit=20")
    suspend fun getAllPokemon(): PokemonDto

    @GET("item?offset=0&limit=20")
    suspend fun getAllItem(): ItemDto

    @GET("pokemon/{name}")
    suspend fun getPokemonDetail(
        @Path("name") name: String,
    ): PokemonDetailDto

    @GET("item/{name}")
    suspend fun getItemDetail(
        @Path("name") name: String,
    ): ItemDetailDto
}