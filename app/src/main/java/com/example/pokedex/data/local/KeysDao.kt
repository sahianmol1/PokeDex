package com.example.pokedex.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.pokedex.models.PokemonKeys

@Dao
interface KeysDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveKeys(keys: kotlin.collections.List<com.example.pokedex.models.PokemonKeys>)

    @Query("SELECT * FROM keys_table WHERE id = :pokemonId")
    suspend fun getKeysByPokemonId(pokemonId: Int): PokemonKeys

    @Query("SELECT * FROM keys_table ORDER BY id DESC")
    suspend fun getAllKeys(): List<PokemonKeys>

    @Query("DELETE FROM keys_table")
    suspend fun clearRemoteKeys()
}