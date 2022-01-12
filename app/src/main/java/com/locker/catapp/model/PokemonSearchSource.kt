package com.locker.catapp.model

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.locker.catapp.model.retrieveAllPokemon

class PokemonSearchSource(private val service: PokeWebService, private val searchString: String) : PagingSource<Int, Pokemon>() {
    companion object {
        const val SEARCH_STRING = ""
        const val PAGE_SIZE = 20
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Pokemon> {
        val position = params.key ?: 0
        val pageSize = params.loadSize ?: PAGE_SIZE

        return try {
            val searchedPokemon = mutableMapOf<String, Pokemon>()
            val allPokemonResponse = service.fetchAllPokemon()
            val allFilteredPokemon = allPokemonResponse.pokemonReferences.filterIndexed { index, pokemonReference ->
                index >= position && pokemonReference.name.startsWith(searchString, ignoreCase = true)
            }
            val filteredPokemon = AllPokemonResponse(
                allPokemonResponse.count,
                allPokemonResponse.nextUrl,
                allPokemonResponse.prevUrl,
                allFilteredPokemon.subList(position, Math.min(allFilteredPokemon.size, position + pageSize))
            )
            retrieveAllPokemon(service, filteredPokemon) { searchedPokemon[it.name] = it }

            LoadResult.Page(
                data = filteredPokemon.pokemonReferences.mapNotNull { searchedPokemon[it.name] },
                prevKey = if (position == 0) null else position - pageSize,
                nextKey = if (position + pageSize >= allFilteredPokemon.size) null else position + pageSize
            )
        } catch (exception: Exception) {
            LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Pokemon>): Int? {
        TODO("Not yet implemented")
    }
}