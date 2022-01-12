package com.locker.catapp.model

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

suspend fun retrieveAllPokemon(
    pokeWebService: PokeWebService,
    allPokemonResponse: AllPokemonResponse,
    pokemonRetrieved: suspend (Pokemon) -> Unit
) =
    coroutineScope {
        val references = allPokemonResponse.pokemonReferences
        references.forEach { ref ->
            launch {
                pokemonRetrieved(pokeWebService.fetchPokemon(ref.url))
            }
        }
    }