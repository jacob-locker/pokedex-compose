package com.locker.catapp.model

import com.google.gson.annotations.SerializedName

data class AllPokemonResponse(
    @SerializedName("count") val count: Int,
    @SerializedName("next") val nextUrl: String?,
    @SerializedName("previous") val prevUrl: String?,
    @SerializedName("results") val pokemonReferences: List<PokemonReference>
) {
    fun nextPage() = getPage(nextUrl)

    fun prevPage() = getPage(prevUrl)

    private fun getPage(url: String?) = url?.split("offset=")?.get(1)?.split("&")?.get(0)?.toIntOrNull()
}