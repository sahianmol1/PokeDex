package com.example.pokedex.ui.fragments.pokemonList

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.pokedex.databinding.LayoutListPokemonBinding
import com.example.pokedex.models.Pokemon
import com.example.pokedex.models.PokemonResponse

class PokemonAdapter(val listener: OnAdapterItemClickListener) : PagingDataAdapter<Pokemon, PokemonAdapter.PokemonViewHolder>(POKEMON_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonViewHolder {
        val binding = LayoutListPokemonBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PokemonViewHolder(binding)
    }


    override fun onBindViewHolder(holder: PokemonViewHolder, position: Int) {
        val currentItem = getItem(position)
        if (currentItem != null) {
            holder.bind(currentItem)
        }
    }

    inner class PokemonViewHolder(private val binding: LayoutListPokemonBinding) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val item = getItem(position)
                    if (item != null) {
                        listener.onItemClick(item)
                    }
                }
            }
        }

        fun bind(pokemon: Pokemon?) {
            binding.apply {
                if (pokemon != null) {
                    textViewPokemonName.text = pokemon.name

                    Glide.with(root)
                            .load(pokemon.getImageUrl())
                            .into(imageViewPokemon)
                }
            }
        }
    }


     companion object {
        private val POKEMON_COMPARATOR = object : DiffUtil.ItemCallback<Pokemon>() {
            override fun areItemsTheSame(oldItem: Pokemon, newItem: Pokemon): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Pokemon, newItem: Pokemon): Boolean {
                return oldItem == newItem
            }

        }
    }


}