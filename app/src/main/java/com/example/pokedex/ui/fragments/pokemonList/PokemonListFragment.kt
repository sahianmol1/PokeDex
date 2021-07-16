package com.example.pokedex.ui.fragments.pokemonList

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.ExperimentalPagingApi
import com.example.pokedex.R
import com.example.pokedex.databinding.FragmentPokemonListBinding
import com.example.pokedex.models.Pokemon
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PokemonListFragment : Fragment(R.layout.fragment_pokemon_list), OnAdapterItemClickListener {

    private val viewModel: PokemonViewModel by viewModels()
    private var _binding: FragmentPokemonListBinding? = null
    private val binding get() = _binding!!

    @ExperimentalPagingApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentPokemonListBinding.bind(view)
        val adapter = PokemonAdapter(this)

        binding.progressBar.visibility = View.VISIBLE

        lifecycleScope.launch {
            viewModel.getAllPokemon().asLiveData().observe(viewLifecycleOwner, Observer {
                binding.progressBar.visibility = View.GONE
                adapter.submitData(viewLifecycleOwner.lifecycle, it)
            })
        }

        binding.apply {
            recyclerViewPokemonList.setHasFixedSize(true)
            recyclerViewPokemonList.adapter = adapter

            recyclerViewPokemonList.adapter = adapter.withLoadStateHeaderAndFooter(
                header = PokemonLoadingAdapter { adapter.retry() },
                footer = PokemonLoadingAdapter { adapter.retry() }
            )
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onItemClick(pokemon: Pokemon) {
        val action = PokemonListFragmentDirections.actionPokemonListFragment2ToPokemonDetailsFragment(pokemon)
        findNavController().navigate(action)
    }
}