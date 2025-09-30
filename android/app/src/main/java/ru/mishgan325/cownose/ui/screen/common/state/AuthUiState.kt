package ru.mishgan325.cownose.ui.screen.common.state

data class AuthUiState(
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val farm: List<String> = listOf("Ферма 1", "Ферма 2", "Ферма 3"),
    val selectedFarm: String = "",
    val farmsExpanded: Boolean = false,
    val error: String? = null,
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false
)