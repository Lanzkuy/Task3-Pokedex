package com.lans.task3_pokedex.presentation.signin

data class SignInUIState(
    var isLoggedIn: Boolean = false,
    var error: String = "",
    var isLoading: Boolean = false,
)
