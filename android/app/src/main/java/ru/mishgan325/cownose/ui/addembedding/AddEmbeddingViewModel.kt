package ru.mishgan325.cownose.ui.addembedding

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ru.mishgan325.cownose.data.network.NetworkNoseRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.mishgan325.cownose.data.network.RequestResult
import ru.mishgan325.cownose.domain.entities.toDomain

sealed class UiState {
    data class Default(val imageUri: Uri?) : UiState()
    object Error : UiState()
}

val TAG = "AddEmbeddingViewModel"

class AddEmbeddingViewModel(
    private val noseRepository: NetworkNoseRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState>(UiState.Default(null))
    val uiState = _uiState.asStateFlow()

    fun setImageForRecognition(imageUri: Uri) {
        noseRepository.chosenImageUri = imageUri.toString()
    }

    fun setPreviewImage(imageUri: Uri?) {
        _uiState.update { UiState.Default(imageUri) }
    }

    fun addEmbedding(imageData: ByteArray, imageUri: Uri) {
        viewModelScope.launch {
            val response = noseRepository.addEmbedding(imageData)

            when (response) {
                is RequestResult.Success -> {
                    _uiState.update { UiState.Default(imageUri) }
                    Log.d(TAG, "Embedding added successfully")
                }

                is RequestResult.Failure -> {
                    _uiState.update { UiState.Error }
                    Log.e(TAG, "Error adding embedding: $response")
                }
            }
        }
    }
}
