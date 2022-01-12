package com.locker.catapp.model

import com.google.gson.annotations.SerializedName

data class Pokemon(
    @SerializedName("id") val id: Int,
    @SerializedName("height") val height: Int,
    @SerializedName("weight") val weight: Int,
    @SerializedName("name") val name: String,
    @SerializedName("order") val order: Int,
    @SerializedName("stats") val stats: List<Stat>,
    @SerializedName("sprites") val sprites: Sprites,
    @SerializedName("types") val types: List<Type>
) {

    companion object {
        val BULBASAUR = Pokemon(
            id = 1,
            height = 7,
            weight = 69,
            name = "bulbasaur",
            order = 1,
            stats = listOf(
                Stat(
                    baseStat = 45,
                    type = StatType(name = "hp")
                ),
                Stat(
                    baseStat = 49,
                    type = StatType(name = "attack")
                ),
                Stat(
                    baseStat = 49,
                    type = StatType(name = "defense")
                ),
                Stat(
                    baseStat = 65,
                    type = StatType(name = "special-attack")
                ),
                Stat(
                    baseStat = 65,
                    type = StatType(name = "special-defense")
                ),
                Stat(
                    baseStat = 45,
                    type = StatType(name = "speed")
                )
            ),
            sprites = Sprites(backDefaultUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/back/1.png",
            backFemaleUrl = "",
            backShinyUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/back/shiny/1.png",
            backShinyFemaleUrl = "",
            frontDefaultUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/1.png",
            frontFemaleUrl = "",
            frontShinyUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/shiny/1.png",
            frontShinyFemaleUrl = ""),
            types = listOf(
                Type(
                    info = TypeInfo(
                        name = "grass"
                    )
                ),
                Type(
                    info = TypeInfo(
                        name = "poison"
                    )
                )
            )
        )
    }
}


