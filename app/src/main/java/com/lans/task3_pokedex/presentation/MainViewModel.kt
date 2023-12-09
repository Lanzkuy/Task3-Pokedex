package com.lans.task3_pokedex.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lans.task3_pokedex.common.Resource
import com.lans.task3_pokedex.domain.repository.IUserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val userRepository: IUserRepository,
) : ViewModel() {
    private val _session = MutableStateFlow<Boolean?>(null)
    val session: Flow<Boolean?> get() = _session

    private val _error = MutableStateFlow<String?>(null)
    val error: Flow<String?> get() = _error

    private val _loading = MutableStateFlow(false)
    val loading: Flow<Boolean> get() = _loading

    init {
        checkSession()
    }

    private fun checkSession() {
        viewModelScope.launch {
            userRepository.getSession().collect { result ->
                when (result) {
                    is Resource.Success -> {
                        _session.value = result.data != 0
                        _loading.value = false
                    }

                    is Resource.Error -> {
                        _error.value = result.message
                        _loading.value = false
                    }

                    is Resource.Loading -> {
                        _loading.value = true
                    }

                    else -> Unit
                }
            }
        }
    }

    fun clearSession() {
        viewModelScope.launch {
            userRepository.clear()
        }
    }
}