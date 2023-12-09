package com.lans.task3_pokedex.presentation.signin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lans.task3_pokedex.common.Resource
import com.lans.task3_pokedex.domain.repository.IAuthRepository
import com.lans.task3_pokedex.domain.repository.IUserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val authRepository: IAuthRepository,
    private val userRepository: IUserRepository,
) : ViewModel() {
    private val _state = MutableStateFlow(SignInUIState())
    val state: Flow<SignInUIState> get() = _state

    fun signin(username: String, password: String) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            authRepository.signIn(username, password).collect { result ->
                when (result) {
                    is Resource.Success -> {
                        userRepository.saveSession(result.data.id)
                        _state.value = _state.value.copy(isLoggedIn = true)
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
}