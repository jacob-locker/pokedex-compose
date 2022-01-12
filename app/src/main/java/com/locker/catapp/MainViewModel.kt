package com.locker.catapp

import androidx.lifecycle.*
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.locker.catapp.model.Pokemon
import com.locker.catapp.model.PokeWebService
import com.locker.catapp.model.PokemonPagingSource
import com.locker.catapp.model.PokemonSearchSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val pokeWebService: PokeWebService
) : ViewModel() {

    val pokemonList = Pager(PagingConfig(pageSize = 20)) {
        PokemonPagingSource(pokeWebService)
    }.flow.cachedIn(viewModelScope)

    val pokemonSearchLiveData: LiveData<PagingData<Pokemon>>
    get() = _pokemonSearchLiveData
    private val _pokemonSearchLiveData = MutableLiveData<PagingData<Pokemon>>()

    val isLoading: LiveData<Boolean>
    get() = _isLoading
    private val _isLoading = MutableLiveData<Boolean>()

    private var searchJob: Job? = null

//    init {
//        searchPokemon("")
//    }

    fun searchPokemon(searchString: String) {
        _isLoading.value = true
        _pokemonSearchLiveData.value = PagingData.empty()
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            searchString.let { text ->
                Pager(PagingConfig(pageSize = 10)) {
                   PokemonSearchSource(pokeWebService, searchString = text)
                }.flow.cachedIn(viewModelScope).collect {
                    _isLoading.value = false
                    _pokemonSearchLiveData.value = it
                }
            }
        }
    }

    data class PokeSearchState(val searchData: PagingData<Pokemon>, val isLoading: Boolean)
    data class PokeUiState(val pokemonList: List<Pokemon>)
}