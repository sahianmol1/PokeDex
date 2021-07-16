package com.example.pokedex.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.pokedex.models.Pokemon
import com.example.pokedex.models.PokemonKeys

@Database(entities = [Pokemon::class, PokemonKeys::class], version = 1, exportSchema = false)
abstract class PokemonDatabase: RoomDatabase() {

    abstract fun getPokemonDao() : PokemonDao
    abstract fun getKeysDao() : KeysDao
}