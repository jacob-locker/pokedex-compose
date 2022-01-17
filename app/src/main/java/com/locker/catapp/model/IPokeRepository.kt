package com.locker.catapp.model

import androidx.paging.PagingData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

interface IPokeRepository {
    fun searchPokemon(searchString: String): Flow<PagingData<Pokemon>>
    fun hasPokemon(searchString: String): Flow<Boolean>
    val retrievalProgressFlow: Flow<Float>
    fun downloadAllPokemon(coroutineScope: CoroutineScope, onFinished: () -> Unit)
}