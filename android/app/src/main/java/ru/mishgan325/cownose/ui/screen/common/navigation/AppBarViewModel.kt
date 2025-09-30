package ru.mishgan325.cownose.ui.screen.common.appbar

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.mishgan325.cownose.data.network.repository.AuthRepository
import javax.inject.Inject

@HiltViewModel
class AppBarViewModel @Inject constructor(private val authRepository: AuthRepository) :
    ViewModel() {

    fun logout() = authRepository.logout()

    fun cleanUp() = authRepository.cleanUp()
}