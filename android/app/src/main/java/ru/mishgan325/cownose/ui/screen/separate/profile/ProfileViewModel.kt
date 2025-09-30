package ru.mishgan325.cownose.ui.screen.separate.profile

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import ru.mishgan325.cownose.ui.screen.common.state.UiState
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor() : ViewModel() {
    private val _uiState = MutableStateFlow(value = UiState.Default(imageUri = null))
    val uiState = _uiState.asStateFlow()
}