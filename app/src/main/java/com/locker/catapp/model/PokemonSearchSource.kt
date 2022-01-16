package com.locker.catapp.model

import androidx.paging.PagingSource
import androidx.paging.PagingState

class PokemonSearchSource(
    private val service: PokeWebService,
    private val searchString: String,
    private var cachedAllPokemonResponse: AllPokemonResponse?,
    private val cache: MutableMap<String, Pokemon>
) : PagingSource<Int, Pokemon>() {
    companion object {
        const val SEARCH_STRING = ""
        const val PAGE_SIZE = 20
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Pokemon> {
        val position = params.key ?: 0
        val pageSize = params.loadSize ?: PAGE_SIZE

        return try {
            if (cachedAllPokemonResponse == null) {
                cachedAllPokemonResponse = service.fetchAllPokemon()
            }

            val allFilteredPokemon =
                cachedAllPokemonResponse!!.pokemonReferences.filterIndexed { index, pokemonReference ->
                    searchString.isEmpty() || (index >= position && pokemonReference.name.startsWith(
                        searchString,
                        ignoreCase = true
                    ))
                }
            val filteredPokemon = AllPokemonResponse(
                cachedAllPokemonResponse!!.count,
                cachedAllPokemonResponse!!.nextUrl,
                cachedAllPokemonResponse!!.prevUrl,
                allFilteredPokemon.subList(
                    position,
                    Math.min(allFilteredPokemon.size, position + pageSize)
                )
            )
            retrieveAllPokemon(service, filteredPokemon, pokeCache = cache)

            if (filteredPokemon.pokemonReferences.isEmpty()) {
                LoadResult.Error(NoSuchPokemonException())
            } else {
                LoadResult.Page(
                    data = filteredPokemon.pokemonReferences.mapNotNull { cache[it.name] }.sortedBy { it.id },
                    prevKey = if (position == 0) null else position - pageSize,
                    nextKey = if (position + pageSize >= allFilteredPokemon.size) null else position + pageSize
                )
            }
        } catch (exception: Exception) {
            LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Pokemon>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}