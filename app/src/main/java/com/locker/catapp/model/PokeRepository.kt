package com.locker.catapp.model

import androidx.paging.Pager
import androidx.paging.PagingConfig
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class PokeRepository @Inject constructor(
    private val pokeWebService: PokeWebService
) : IPokeRepository {
    private var cachedAllPokemonResponse: AllPokemonResponse? = null
    private val pokemonCache = mutableMapOf<String, Pokemon>()

    override fun searchPokemon(searchString: String) = Pager(PagingConfig(pageSize = 20)) {
        PokemonSearchSource(pokeWebService,
            searchString = searchString,
            cachedAllPokemonResponse = cachedAllPokemonResponse,
            cache = pokemonCache)
    }.flow

    override fun hasPokemon(searchString: String) = flow {
        if (cachedAllPokemonResponse == null) {
            cachedAllPokemonResponse = pokeWebService.fetchAllPokemon()
        }

        emit(cachedAllPokemonResponse?.pokemonReferences?.find { it.name.startsWith(searchString) } != null)
    }
}