package com.example.pokedex.ui.fragments.pokemonList

import com.example.pokedex.models.Pokemon

interface OnAdapterItemClickListener {
    fun onItemClick(pokemon: Pokemon)
}