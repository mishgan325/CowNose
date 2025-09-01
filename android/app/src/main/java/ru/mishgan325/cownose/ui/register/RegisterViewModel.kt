package ru.mishgan325.cownose.ui.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.mishgan325.cownose.data.database.AuthRepository
import ru.mishgan325.cownose.ui.common.AuthUiState

class RegisterViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState = _uiState.asStateFlow()

    fun onEmailChange(newEmail: String) {
        _uiState.value = _uiState.value.copy(email = newEmail)
    }

    fun onPasswordChange(newPassword: String) {
        _uiState.value = _uiState.value.copy(password = newPassword)
    }

    fun onConfirmPasswordChange(newPassword: String) {
        _uiState.value = _uiState.value.copy(confirmPassword = newPassword)
    }

    fun onFarmChange(newFarm: String) {
        _uiState.value = _uiState.value.copy(farm = newFarm)
    }

    fun register() {
        val (email, password, confirmPassword) = _uiState.value

        if (password != confirmPassword) {
            _uiState.value = _uiState.value.copy(error = "Пароли не совпадают")
            return
        }

        _uiState.value = _uiState.value.copy(isLoading = true, error = null)

        viewModelScope.launch {
            val result = authRepository.register(email, password)
            _uiState.value = if (result.isSuccess) {
                _uiState.value.copy(isLoading = false, isSuccess = true)
            } else {
                _uiState.value.copy(
                    isLoading = false,
                    error = result.exceptionOrNull()?.message
                )
            }
        }
    }
}
