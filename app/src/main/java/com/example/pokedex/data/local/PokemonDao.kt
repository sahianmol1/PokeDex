package com.example.pokedex.data.local

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.pokedex.models.Pokemon

@Dao
interface PokemonDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveAllPokemon(pokemons: List<Pokemon>)

    @Query("SELECT * FROM pokemon_table")
    fun getAllPokemon(): PagingSource<Int, Pokemon>

    @Query("DELETE FROM pokemon_table")
    suspend fun clearPokemons()
}