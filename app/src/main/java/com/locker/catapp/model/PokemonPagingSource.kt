package com.locker.catapp.model

import androidx.paging.PagingSource
import androidx.paging.PagingState

class PokemonPagingSource(private val service: PokeWebService,
                          private val cache: MutableMap<String, Pokemon>) : PagingSource<Int, Pokemon>() {

    companion object {
        private const val START_PAGE = 0
        private const val PAGE_SIZE = 20
    }

    private var allPokemon = mutableListOf<Pokemon>()

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Pokemon> {
        val page = params.key ?: START_PAGE
        val pageSize = params.loadSize ?: PAGE_SIZE

        return try {
            allPokemon = mutableListOf()
            val allPokemonResponse = service.fetchAllPokemon(page, pageSize)
            retrieveAllPokemon(service, allPokemonResponse, pokeCache = cache) { allPokemon.add(it) }

            LoadResult.Page(
                data = allPokemon.sortedBy { it.order },
                prevKey = allPokemonResponse.prevPage(),
                nextKey = allPokemonResponse.nextPage()
            )
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