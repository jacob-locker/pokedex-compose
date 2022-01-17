package com.locker.catapp.model

class PokemonMemoryCache : IPokemonMemoryCache {
    override var cachedAllPokemonResponse: AllPokemonResponse?
        get() = _cachedAllResponse
        set(value) { _cachedAllResponse = value }
    private var _cachedAllResponse: AllPokemonResponse? = null

    override val pokemonCache: Map<String, Pokemon>
        get() = _pokemonCache
    private val _pokemonCache = mutableMapOf<String, Pokemon>()

    override fun set(pokemonName: String, pokemon: Pokemon) {
        _pokemonCache[pokemonName] = pokemon
    }

    override fun get(pokemonName: String): Pokemon? = _pokemonCache[pokemonName]

    override fun contains(name: String) = _pokemonCache.containsKey(name)
}