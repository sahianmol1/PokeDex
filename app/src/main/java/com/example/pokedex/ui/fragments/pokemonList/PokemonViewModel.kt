package com.example.pokedex.ui.fragments.pokemonList

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.pokedex.data.PokemonRepository
import com.example.pokedex.models.Pokemon
import com.example.pokedex.models.PokemonInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class PokemonViewModel @ViewModelInject constructor(private val repository: PokemonRepository) : ViewModel() {

    @ExperimentalPagingApi
    fun getAllPokemon(): Flow<PagingData<Pokemon>> = repository.getAllPokemon().cachedIn(viewModelScope)

    suspend fun getPokemonInfo(pokemonName: String) =
            repository.getPokemonInfo(pokemonName)


}