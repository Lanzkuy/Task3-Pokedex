package com.lans.task3_pokedex.presentation.pokemon

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.lans.task3_pokedex.databinding.FragmentPokemonBinding
import com.lans.task3_pokedex.domain.model.PokedexEntry
import com.lans.task3_pokedex.presentation.MainActivity
import com.lans.task3_pokedex.presentation.adapter.PokemonAdapter
import com.lans.task3_pokedex.utils.SpacesItemDecoration
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PokemonFragment : Fragment() {
    private lateinit var binding: FragmentPokemonBinding
    private val viewModel: PokemonViewModel by viewModels()

    private var pokemonData = emptyList<PokedexEntry>()
    private lateinit var rvPokemon: RecyclerView
    private lateinit var pokemonAdapter: PokemonAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentPokemonBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeComponent()
        lifecycleScope.launch {
            observePokemon()
        }
    }

    private fun initializeComponent() {
        rvPokemon = binding.rvPokemon
        rvPokemon.layoutManager = GridLayoutManager(requireActivity(), 2)
        rvPokemon.addItemDecoration(SpacesItemDecoration(20))
        (requireActivity() as MainActivity).searchView.setOnQueryTextListener(object :
            SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(text: String?): Boolean {
                if (!text.isNullOrBlank()) {
                    filterPokemon(text)
                } else {
                    pokemonAdapter.filter(pokemonData)
                }
                return false
            }
        })
    }

    private suspend fun observePokemon() {
        viewModel.state.collect { result ->
            (requireActivity() as MainActivity).showLoading(result.isLoading)

            if (result.data.isNotEmpty()) {
                result.data.map { pokemon ->
                    pokemon.isLiked = result.favorite.contains(pokemon.pokemonName)
                }
                pokemonData = result.data
                pokemonAdapter = PokemonAdapter(result.data, onClick = { name ->
                    viewModel.createFavorite(1, name)
                })
                rvPokemon.adapter = pokemonAdapter
            }

            if (result.error.isNotBlank()) {
                Snackbar.make(binding.root, result.error, Snackbar.LENGTH_SHORT).show()
                result.error = ""
            }
        }
    }

    private fun filterPokemon(pokemon: String) {
        val filter = pokemonData.filter { it.pokemonName.contains(pokemon) }
        pokemonAdapter.filter(filter)
    }
}