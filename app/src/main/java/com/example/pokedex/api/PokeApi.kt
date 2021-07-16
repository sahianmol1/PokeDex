package com.example.pokedex.api

import androidx.lifecycle.LiveData
import com.example.pokedex.models.PokemonInfo
import com.example.pokedex.models.PokemonResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PokeApi {

    companion object{
        const val BASE_URL = "https://pokeapi.co/api/v2/"
        const val PAGING_SIZE = 30
    }

    @GET("pokemon/")
    suspend fun getAllPokemon(
        @Query("limit") size: Int = PAGING_SIZE,
        @Query("offset") offsetSize: Int? = PAGING_SIZE
    ): PokemonResponse


    @GET("pokemon/{name}")
    suspend fun getPokemonInfo(
        @Path("name") name: String
    ): PokemonInfo

}