package com.lans.task3_pokedex.presentation.signup

data class SignUpUIState(
    var isSuccess: Boolean = false,
    var error: String = "",
    var isLoading: Boolean = false,
)
