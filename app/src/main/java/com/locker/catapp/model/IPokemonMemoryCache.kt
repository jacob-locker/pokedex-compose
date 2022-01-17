package com.locker.catapp.model

interface IPokemonMemoryCache {
    var cachedAllPokemonResponse: AllPokemonResponse?
    val pokemonCache: Map<String, Pokemon>

    operator fun set(pokemonName: String, pokemon: Pokemon)
    operator fun get(pokemonName: String): Pokemon?

    operator fun contains(name: String): Boolean
}