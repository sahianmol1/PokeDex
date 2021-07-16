package com.example.pokedex.data.remote

import androidx.paging.PagingSource
import com.example.pokedex.api.PokeApi
import com.example.pokedex.api.PokeApi.Companion.PAGING_SIZE
import com.example.pokedex.models.Pokemon
import com.example.pokedex.models.PokemonResponse
import retrofit2.HttpException
import java.io.IOException

const val POKEMON_STARTING_PAGE_INDEX = 1

class PokemonPagingSource(
    private val pokeApi: PokeApi
) : PagingSource<Int, Pokemon>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Pokemon> {
        val position = params.key ?: POKEMON_STARTING_PAGE_INDEX
        return try {
            val response = pokeApi.getAllPokemon(
                offsetSize = position * PAGING_SIZE
            )
            val pokemons = response.results
            LoadResult.Page(
                    data = pokemons,
                    prevKey = if(position == POKEMON_STARTING_PAGE_INDEX) null else position - 1,
                    nextKey = if(pokemons.isEmpty()) null else position + 1
            )

        } catch (e: IOException) {
            LoadResult.Error(e)
        } catch (e: HttpException) {
            LoadResult.Error(e)
        }
    }
}