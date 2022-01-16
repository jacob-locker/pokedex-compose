package com.locker.catapp.model

import kotlinx.coroutines.delay

class FakePokeWebService(private val config: Config = Config()) : PokeWebService {

    override suspend fun fetchAllPokemon(currentPage: Int, pageSize: Int): AllPokemonResponse {
        delay(config.allPokemonDelayMillis)
        return ALL_POKEMON
    }

    override suspend fun fetchPokemon(url: String): Pokemon {
        delay(config.pokemonDelayMillis)
        return Pokemon.BULBASAUR
    }

    data class Config(val allPokemonDelayMillis: Long = 500,
                      val pokemonDelayMillis: Long = 2000)

    companion object {
        val ALL_POKEMON: AllPokemonResponse = AllPokemonResponse(0, null, null,
            listOf(
                PokemonReference(
                    "bulbasaur",
                    "1"
                ),
                PokemonReference(
                    "squirtle",
                    "2"
                ),
                PokemonReference(
                    "charmander",
                    "3"
                )
            )
        )
    }
}