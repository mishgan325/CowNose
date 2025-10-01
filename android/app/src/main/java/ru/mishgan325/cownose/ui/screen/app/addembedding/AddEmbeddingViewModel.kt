package ru.mishgan325.cownose.ui.screen.app.addembedding

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.mishgan325.cownose.data.network.repository.CowNoseRepository
import ru.mishgan325.cownose.data.network.request.RequestResult
import ru.mishgan325.cownose.ui.screen.common.state.UiState
import ru.mishgan325.cownose.ui.utils.ImageLoader
import javax.inject.Inject

@HiltViewModel
class AddEmbeddingViewModel @Inject constructor(
    private val cowNoseRepository: CowNoseRepository,
    private val imageLoader: ImageLoader
) : ViewModel() {
    private val _uiState = MutableStateFlow<UiState>(value = UiState.Default(imageUri = null))
    val uiState = _uiState.asStateFlow()

    fun setPreviewImage(imageUri: Uri?) {
        _uiState.update { UiState.Default(imageUri) }
    }

    fun addEmbedding(name: String, imageUri: Uri) {
        viewModelScope.launch {
            val imageData = imageLoader.load(imageUri)

            if (imageData != null) {
                val response = cowNoseRepository.addEmbedding(name = name, file = imageData)

                when (response) {
                    is RequestResult.Success -> _uiState.update { UiState.Default(imageUri = imageUri) }

                    is RequestResult.Failure -> _uiState.update { UiState.Error(message = it.toString()) }
                }
            } else
                _uiState.update { UiState.Error(message = it.toString()) }
        }
    }
}