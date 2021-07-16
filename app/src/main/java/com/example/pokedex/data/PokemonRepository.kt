package com.example.pokedex.data

import androidx.lifecycle.LiveData
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.example.pokedex.api.PokeApi
import com.example.pokedex.data.local.PokemonDatabase
import com.example.pokedex.data.remote.PokemonPagingSource
import com.example.pokedex.data.remote.PokemonRemoteMediator
import com.example.pokedex.models.PokemonInfo
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PokemonRepository @Inject constructor(
    private val pokeApi: PokeApi,
    private val database: PokemonDatabase
) {

    @ExperimentalPagingApi
    fun getAllPokemon() =
        Pager(
            config = PagingConfig(
                pageSize = 30,
                enablePlaceholders = false,
                prefetchDistance = 3
            ),
            remoteMediator = PokemonRemoteMediator(pokeApi, database),
            pagingSourceFactory = { database.getPokemonDao().getAllPokemon() }
        ).flow

    suspend fun getPokemonInfo(pokemonName: String) = pokeApi.getPokemonInfo(pokemonName)

}