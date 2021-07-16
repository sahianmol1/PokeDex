package com.example.pokedex.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "keys_table")
data class PokemonKeys(
    @PrimaryKey (autoGenerate = true)
    val id: Int = 0,
    val prevKey: Int?,
    val nextKey: Int?
)