package com.locker.catapp.model

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow

interface IPokeRepository {
    fun searchPokemon(searchString: String): Flow<PagingData<Pokemon>>
    fun hasPokemon(searchString: String): Flow<Boolean>
}