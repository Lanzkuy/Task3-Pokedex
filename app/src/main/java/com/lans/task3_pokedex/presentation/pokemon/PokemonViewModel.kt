package com.lans.task3_pokedex.presentation.pokemon

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lans.task3_pokedex.common.Resource
import com.lans.task3_pokedex.domain.repository.IFavoriteRepository
import com.lans.task3_pokedex.domain.repository.IPokemonRepository
import com.lans.task3_pokedex.domain.repository.IUserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PokemonViewModel @Inject constructor(
    private val pokemonRepository: IPokemonRepository,
    private val favoriteRepository: IFavoriteRepository,
    private val userRepository: IUserRepository
) : ViewModel() {
    private val _state = MutableStateFlow(PokemonUIState())
    val state: Flow<PokemonUIState> = _state

    init {
        loadAllPokemon()
        loadAllFavoritePokemon()
    }

    fun createFavorite(type: Int, name: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val userId = userRepository.getSession().first().toString()
            favoriteRepository.create(userId, type, name)
        }
    }

    private fun loadAllPokemon() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            pokemonRepository.getAllPokemon().collect { result ->
                when (result) {
                    is Resource.Success -> {
                        _state.value = _state.value.copy(data = result.data)
                        _state.value = _state.value.copy(isLoading = false)
                    }

                    is Resource.Error -> {
                        _state.value = _state.value.copy(error = result.message)
                        _state.value = _state.value.copy(isLoading = false)
                    }

                    is Resource.Loading -> {
                        _state.value = _state.value.copy(isLoading = true)
                    }

                    else -> Unit
                }
            }
        }
    }

    private fun loadAllFavoritePokemon() {
        viewModelScope.launch {
            val userId = userRepository.getSession().first().toString()
            favoriteRepository.getAllFavoritePokemon(userId).collect { result ->
                when (result) {
                    is Resource.Success -> {
                        _state.value = _state.value.copy(favorite = result.data)
                    }

                    is Resource.Error -> {
                        _state.value = _state.value.copy(error = result.message)
                        _state.value = _state.value.copy(isLoading = false)
                    }

                    is Resource.Loading -> {
                        _state.value = _state.value.copy(isLoading = true)
                    }

                    else -> Unit
                }
            }
        }
    }
}