package com.locker.catapp.model

import androidx.paging.Config
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class PokemonPagingSourceTest {
    @Before
    fun setUp() {

    }

    @ExperimentalCoroutinesApi
    @Test
    fun testSourceFlow() = runBlocking {
        val startTimeMillis = System.currentTimeMillis()
        PokemonPagingSource(FakePokeWebService()).load(
            PagingSource.LoadParams.Refresh(
                key = 0,
                loadSize = FakePokeWebService.ALL_POKEMON.pokemonReferences.size,
                placeholdersEnabled = false
            )
        )

        println("Test Complete in ${System.currentTimeMillis() - startTimeMillis} milliseconds.")
    }
}