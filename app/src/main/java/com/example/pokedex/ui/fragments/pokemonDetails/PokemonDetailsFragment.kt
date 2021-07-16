package com.example.pokedex.ui.fragments.pokemonDetails

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.pokedex.R
import com.example.pokedex.databinding.FragmentPokemonDetailsBinding
import com.example.pokedex.ui.fragments.pokemonList.PokemonViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PokemonDetailsFragment: Fragment(R.layout.fragment_pokemon_details) {

    private val args by navArgs<PokemonDetailsFragmentArgs>()
    private val viewModel: PokemonViewModel by viewModels()
    private var _binding: FragmentPokemonDetailsBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentPokemonDetailsBinding.bind(view)

        val pokemonName = args.pokemon.name
        val pokemonImage = args.pokemon.getImageUrl()

        viewLifecycleOwner.lifecycleScope.launch {
            val pokemonInfo = viewModel.getPokemonInfo(pokemonName)
                binding.apply {
                    tvPokemonName.text = pokemonName
                    tvWeightInfo.text = pokemonInfo.weight.toString()
                    tvHeightInfo.text = pokemonInfo.height.toString()
                    tvPokemonType.text = pokemonInfo.types[0].type.name.toString()
                    Glide.with(view)
                            .load(pokemonImage)
                            .into(ivPokemon)
                }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}