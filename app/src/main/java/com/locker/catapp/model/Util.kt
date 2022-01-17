package com.locker.catapp.model

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import retrofit2.HttpException

suspend fun PokeWebService.retrieveAllPokemon(
    allPokemonResponse: AllPokemonResponse,
    pokeCache: IPokemonMemoryCache,
    pokemonRetrieved: suspend (Pokemon) -> Unit = {}
) =
    coroutineScope {
        val references = allPokemonResponse.pokemonReferences
        references.forEach { ref ->
            launch(Dispatchers.IO) {
                try {
                    val pokemon = if (ref.name in pokeCache) pokeCache[ref.name]!! else fetchPokemon(ref.url)
                    pokeCache[ref.name] = pokemon
                    pokemonRetrieved(pokemon)
                } catch (e: HttpException) {}
            }
        }
    }

fun Context.hasNetwork():  Boolean? {
    var isConnected: Boolean? = false // Initial Value
    val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
    if (activeNetwork != null && activeNetwork.isConnected)
        isConnected = true
    return isConnected
}