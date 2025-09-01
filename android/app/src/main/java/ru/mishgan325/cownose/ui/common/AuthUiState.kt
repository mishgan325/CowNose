package ru.mishgan325.cownose.ui.common

data class AuthUiState(
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val farm: String = "Ферма №1",
    val isLoading: Boolean = false,
    val error: String? = null,
    val isSuccess: Boolean = false
)
