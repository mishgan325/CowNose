package ru.mishgan325.cownose.ui.results

import android.net.Uri
import android.util.Log
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ru.mishgan325.cownose.data.database.LocalNoseRepository
import ru.mishgan325.cownose.data.network.NetworkNoseRepository
import ru.mishgan325.cownose.data.network.RequestResult
import ru.mishgan325.cownose.domain.entities.NoseSearchResult
import ru.mishgan325.cownose.domain.entities.toDomain
import ru.mishgan325.cownose.ui.utlis.ImageLoader
import ru.mishgan325.cownose.ui.utlis.saveBitmapToFile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed class UiState {
    data object InProgress : UiState()
    data class NoseFound(
        val nose: NoseSearchResult,
        val imageUri: Uri,
        val showSavedMessage: Boolean,
    ) : UiState()

    data class NoseNotFound(
        val imageUri: Uri,
        val showSavedMessage: Boolean,

        ) : UiState()
    data object Error : UiState()
}

val TAG = "ResultViewModel"

class ResultViewModel(
    private val networkNoseRepository: NetworkNoseRepository,
    private val localNoseRepository: LocalNoseRepository,
    private val imageLoader: ImageLoader,

    ) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState>(UiState.InProgress)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    init {

        val chosenImageUri = networkNoseRepository.chosenImageUri?.toUri()
        if (chosenImageUri != null) {
            viewModelScope.launch {
                with(Dispatchers.IO) {

                    val imageData = imageLoader.load(chosenImageUri)
//                    val filepath = saveBitmapToFile(chosenImageUri)

                    if (imageData != null) {
                        recognize(imageData, chosenImageUri)
                    }
                }
            }
        }
    }



    fun recognize(imageData: ByteArray, imageUri: Uri) {
        viewModelScope.launch {
            val response =
                networkNoseRepository.detectAndSearch(imageData)
                    .transform { it.toDomain(-1, null) }

            when (response) {
                is RequestResult.Success -> {
                    _uiState.update { UiState.NoseFound(response.data, imageUri, false) }
                }

                is RequestResult.Failure -> {
                    _uiState.update { UiState.NoseNotFound(imageUri, false) }

                }
            }

            Log.d(TAG, "recognition response: $response")
        }
    }

    fun insertNose(nose: NoseSearchResult, imageUri: Uri) {

        viewModelScope.launch {
            with(Dispatchers.IO) {
                val filepath = saveBitmapToFile(imageUri)
                localNoseRepository.insertNoseSearchResult(nose.copy(imageFilepath = filepath))
//                showSavedMessage()
            }
        }
    }


}