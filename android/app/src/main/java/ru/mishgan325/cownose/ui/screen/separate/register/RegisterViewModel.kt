package ru.mishgan325.cownose.ui.screen.separate.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ru.mishgan325.cownose.data.network.repository.AuthRepository
import ru.mishgan325.cownose.ui.screen.common.state.AuthUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class RegisterViewModel @Inject constructor(private val authRepository: AuthRepository) :
    ViewModel() {
    private val _uiState = MutableStateFlow(value = AuthUiState())
    val uiState = _uiState.asStateFlow()

    fun onFarmsExpanded(expanded: Boolean) {
        _uiState.value = _uiState.value.copy(farmsExpanded = expanded)
    }

    fun onFarmChange(farm: String) {
        _uiState.value = _uiState.value.copy(selectedFarm = farm, farmsExpanded = false)
    }

    fun onEmailChange(newEmail: String) {
        _uiState.value = _uiState.value.copy(email = newEmail)
    }

    fun onPasswordChange(newPassword: String) {
        _uiState.value = _uiState.value.copy(password = newPassword)
    }

    fun onConfirmPasswordChange(newPassword: String) {
        _uiState.value = _uiState.value.copy(confirmPassword = newPassword)
    }

    fun register() {
        val (email, password, confirmPassword) = _uiState.value

        if (password != confirmPassword) _uiState.value =
            _uiState.value.copy(error = "Пароли не совпадают")

        _uiState.value = _uiState.value.copy(isLoading = true, error = null)

        viewModelScope.launch {
            val result = authRepository.register(email, password)

            _uiState.value =
                if (result.isSuccess)
                    _uiState.value.copy(isLoading = false, isSuccess = true)
                else
                    _uiState.value.copy(
                        isLoading = false,
                        error = result.exceptionOrNull()?.message
                    )
        }
    }
}