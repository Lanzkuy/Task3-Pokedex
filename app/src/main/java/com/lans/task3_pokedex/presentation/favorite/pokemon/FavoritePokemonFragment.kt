package com.lans.task3_pokedex.presentation.favorite.pokemon

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
import com.lans.task3_pokedex.databinding.FragmentFavoritePokemonBinding
import com.lans.task3_pokedex.domain.model.PokedexEntry
import com.lans.task3_pokedex.presentation.MainActivity
import com.lans.task3_pokedex.presentation.adapter.PokemonAdapter
import com.lans.task3_pokedex.utils.SpacesItemDecoration
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FavoritePokemonFragment : Fragment() {
    private lateinit var binding: FragmentFavoritePokemonBinding
    private val viewModel: FavoritePokemonViewModel by viewModels()

    private var favoritePokemonData = emptyList<PokedexEntry>()
    private lateinit var rvFavoritePokemon: RecyclerView
    private lateinit var favoritePokemonAdapter: PokemonAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentFavoritePokemonBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeComponent()
        lifecycleScope.launch {
            observeFavoritePokemon()
        }
    }

    override fun onResume() {
        super.onResume()
        (requireActivity() as MainActivity).searchView.setOnQueryTextListener(object :
            SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(text: String?): Boolean {
                if (!text.isNullOrBlank()) {
                    filterPokemon(text)
                } else {
                    favoritePokemonAdapter.filter(favoritePokemonData)
                }
                return false
            }
        })
    }

    private fun initializeComponent() {
        rvFavoritePokemon = binding.rvFavoritePokemon
        rvFavoritePokemon.layoutManager = GridLayoutManager(requireActivity(), 2)
        rvFavoritePokemon.addItemDecoration(SpacesItemDecoration(20))
    }

    private suspend fun observeFavoritePokemon() {
        viewModel.state.collect { result ->
            (requireActivity() as MainActivity).showLoading(result.isLoading)

            if (result.data.isNotEmpty()) {
                favoritePokemonData = result.data
                favoritePokemonAdapter = PokemonAdapter(result.data)
                rvFavoritePokemon.adapter = favoritePokemonAdapter
            }

            if (result.error.isNotBlank()) {
                Snackbar.make(binding.root, result.error, Snackbar.LENGTH_SHORT).show()
                result.error = ""
            }
        }
    }

    private fun filterPokemon(pokemon: String) {
        val filter = favoritePokemonData.filter { it.pokemonName.contains(pokemon) }
        favoritePokemonAdapter.filter(filter)
    }
}