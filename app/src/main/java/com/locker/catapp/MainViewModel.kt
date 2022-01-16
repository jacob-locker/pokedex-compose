package com.locker.catapp

import android.os.SystemClock
import androidx.lifecycle.*
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.locker.catapp.model.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val pokeRepository: IPokeRepository
) : ViewModel() {

    companion object {
        const val MIN_LOAD_MILLIS = 300
    }

    val pokemonSearchFlow: StateFlow<PagingData<Pokemon>>
        get() = _pokemonSearchFlow
    private val _pokemonSearchFlow = MutableStateFlow<PagingData<Pokemon>>(PagingData.empty())

    val hasPokemonFlow: StateFlow<Boolean>
        get() = _hasPokemonFlow
    private val _hasPokemonFlow = MutableStateFlow(true)

    private var searchJob: Job? = null

    init {
        searchPokemon("")
    }

    fun searchPokemon(searchString: String) {
//        _pokemonSearchFlow.value = PagingData.empty()
        viewModelScope.launch(Dispatchers.IO) {
            pokeRepository.hasPokemon(searchString).collectLatest {
                _hasPokemonFlow.value = it
            }
        }
        searchJob?.cancel()
        searchJob = viewModelScope.launch(Dispatchers.IO) {
            pokeRepository.searchPokemon(searchString).cachedIn(viewModelScope).collectLatest {
                _pokemonSearchFlow.value = it
            }
        }
    }

    data class PokeSearchState(val searchData: PagingData<Pokemon>, val isLoading: Boolean)
    data class PokeUiState(val pokemonList: List<Pokemon>)
}