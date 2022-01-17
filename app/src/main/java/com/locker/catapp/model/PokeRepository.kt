package com.locker.catapp.model

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicInteger
import javax.inject.Inject

class PokeRepository @Inject constructor(
    @ApplicationContext private val context: Context,
    private val pokeWebService: PokeWebService,
    private val pokeMemCache: IPokemonMemoryCache
) : IPokeRepository {
    companion object {
        const val SHARED_PREF_KEY = "repository"
        const val FINISHED_DOWNLOAD_KEY = "finished_download"
    }

    override val retrievalProgressFlow = MutableStateFlow(0f)
    private val downloadCount = AtomicInteger(0)

    override fun searchPokemon(searchString: String) = Pager(PagingConfig(pageSize = 20)) {
        PokemonSearchSource(pokeWebService,
            searchString = searchString,
            pokeMemoryCache = pokeMemCache)
    }.flow

    override fun hasPokemon(searchString: String) = flow {
        if (pokeMemCache.cachedAllPokemonResponse == null) {
            pokeMemCache.cachedAllPokemonResponse = pokeWebService.fetchAllPokemon()
        }

        emit(pokeMemCache.cachedAllPokemonResponse?.pokemonReferences?.find { it.name.startsWith(searchString) } != null)
    }

    override fun downloadAllPokemon(coroutineScope: CoroutineScope, onFinished: () -> Unit) {
        val sharedPrefs = context.getSharedPreferences(SHARED_PREF_KEY, Context.MODE_PRIVATE)
        if (!sharedPrefs.getBoolean(FINISHED_DOWNLOAD_KEY, false)) {
            coroutineScope.launch(Dispatchers.IO) {
                pokeMemCache.cachedAllPokemonResponse = pokeWebService.fetchAllPokemon()
                if (pokeMemCache.cachedAllPokemonResponse != null) {
                    pokeWebService.retrieveAllPokemon(pokeMemCache.cachedAllPokemonResponse!!, pokeMemCache) {
                        val downloaded = downloadCount.incrementAndGet()
                        if (downloaded == pokeMemCache.cachedAllPokemonResponse!!.count) {
                            sharedPrefs
                                .edit()
                                .putBoolean(FINISHED_DOWNLOAD_KEY, true)
                                .apply()

                            onFinished()
                        }
                        val progress = downloaded.toFloat() / pokeMemCache.cachedAllPokemonResponse!!.count
                        Log.d(PokeRepository::class.java.simpleName, "Progress: $progress")
                        retrievalProgressFlow.value = progress
                    }
                }
            }
        } else {
            retrievalProgressFlow.value = 1f
            onFinished()
        }
    }
}