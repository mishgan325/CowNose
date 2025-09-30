package ru.mishgan325.cownose.ui.screen.separate.splash

import androidx.lifecycle.ViewModel
import ru.mishgan325.cownose.data.network.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(authRepository: AuthRepository) :
    ViewModel() {
    private val _isLoggedIn = authRepository.isLoggedIn
    val isLoggedIn = _isLoggedIn
}