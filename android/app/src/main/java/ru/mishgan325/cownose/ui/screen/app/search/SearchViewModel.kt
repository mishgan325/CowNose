package ru.mishgan325.cownose.ui.screen.app.search

import android.net.Uri
import androidx.lifecycle.ViewModel
import ru.mishgan325.cownose.data.network.repository.CowNoseRepository
import ru.mishgan325.cownose.ui.screen.common.state.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(private val cowNoseRepository: CowNoseRepository) :
    ViewModel() {
    private val _uiState = MutableStateFlow<UiState>(value = UiState.Default(null))
    val uiState = _uiState.asStateFlow()

    fun setImageForRecognition(imageUri: Uri) =
        cowNoseRepository.onChosenImageUri(imageUri = imageUri)

    fun setPreviewImage(imageUri: Uri?) =
        _uiState.update { UiState.Default(imageUri) }
}