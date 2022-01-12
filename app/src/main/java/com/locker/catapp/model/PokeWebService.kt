package com.locker.catapp.model

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Url

interface PokeWebService {
    @GET("pokemon")
    suspend fun fetchAllPokemon(@Query("offset") currentPage: Int = 0, @Query("limit") pageSize: Int = 1118) : AllPokemonResponse

    @GET
    suspend fun fetchPokemon(@Url url: String) : Pokemon
}