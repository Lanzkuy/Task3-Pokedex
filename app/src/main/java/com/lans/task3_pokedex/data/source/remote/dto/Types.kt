package com.lans.task3_pokedex.data.source.remote.dto

import com.squareup.moshi.Json

data class Types(
    @field:Json(name = "slot")
    val slot: Int,
    @field:Json(name = "type")
    val type: TypeDetail,
)
