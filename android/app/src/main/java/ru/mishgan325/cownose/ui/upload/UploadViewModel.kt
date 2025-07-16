package ru.mishgan325.cownose.ui.upload

import android.net.Uri
import androidx.lifecycle.ViewModel
import ru.mishgan325.cownose.data.network.NetworkNoseRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

sealed class UiState {
    data class Default(val imageUri: Uri?) : UiState()
    data object Error : UiState()
}

val TAG = "UploadViewModel"

class UploadViewModel(
    private val noseRepository: NetworkNoseRepository,

    ) : ViewModel() {

    init {
    }


    private val _uiState = MutableStateFlow<UiState>(UiState.Default(null))
    val uiState = _uiState.asStateFlow()


    fun setImageForRecognition(imageUri: Uri) {
        noseRepository.chosenImageUri = imageUri.toString()
    }

    fun setPreviewImage(imageUri: Uri?) {
        _uiState.update { UiState.Default(imageUri) }
    }

}