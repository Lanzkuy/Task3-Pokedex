package com.lans.task3_pokedex.data.source.remote.dto

import com.squareup.moshi.Json

data class Result(
    @field:Json(name = "name")
    val name: String,
    @field:Json(name = "url")
    val url: String,
)
