package com.locker.catapp.model

import com.google.gson.annotations.SerializedName

data class PokemonReference(
    @field:SerializedName("name") val name: String,
    @field:SerializedName("url") val url: String
)
