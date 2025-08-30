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
import ru.mishgan325.cownose.ui.utlis.ImageLoader

sealed class UiState {
    data class Default(val imageUri: Uri?) : UiState()
    object Error : UiState()
}

val TAG = "AddEmbeddingViewModel"

class AddEmbeddingViewModel(
    private val noseRepository: NetworkNoseRepository,
    private val imageLoader: ImageLoader, // Подключаем ImageLoader
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState>(UiState.Default(null))
    val uiState = _uiState.asStateFlow()

    fun setImageForRecognition(imageUri: Uri) {
        noseRepository.chosenImageUri = imageUri.toString()
    }

    fun setPreviewImage(imageUri: Uri?) {
        _uiState.update { UiState.Default(imageUri) }
    }

    fun addEmbedding(name: String, imageUri: Uri) {
        viewModelScope.launch {
            // Загружаем изображение
            val imageData = imageLoader.load(imageUri)

            // Если изображение загружено, продолжаем
            if (imageData != null) {
                val response = noseRepository.addEmbedding(name,imageData)

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
            } else {
                Log.e(TAG, "Error loading image for embedding")
                _uiState.update { UiState.Error }
            }
        }
    }
}
