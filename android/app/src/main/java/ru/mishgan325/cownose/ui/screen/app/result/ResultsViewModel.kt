package ru.mishgan325.cownose.ui.screen.app.result

import android.content.Context
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ru.mishgan325.cownose.data.database.entity.NoseSearchResult
import ru.mishgan325.cownose.data.database.entity.toDomain
import ru.mishgan325.cownose.data.network.repository.CowNoseRepository
import ru.mishgan325.cownose.data.network.repository.LocalNoseRepository
import ru.mishgan325.cownose.data.network.request.RequestResult
import ru.mishgan325.cownose.ui.screen.common.state.UiState
import ru.mishgan325.cownose.ui.utils.ImageLoader
import ru.mishgan325.cownose.ui.utils.saveBitmapToFile
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@RequiresApi(value = Build.VERSION_CODES.O)
@HiltViewModel
class ResultsViewModel @Inject constructor(
    @param:ApplicationContext private val context: Context,
    private val cowNoseRepository: CowNoseRepository,
    private val localNoseRepository: LocalNoseRepository,
    private val imageLoader: ImageLoader,
) : ViewModel() {
    private val _uiState = MutableStateFlow<UiState>(value = UiState.InProgress)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    init {
        val chosenImageUri = cowNoseRepository.chosenImageUri.value
        if (chosenImageUri != null) {
            viewModelScope.launch {
                with(receiver = Dispatchers.IO) {
                    val imageData = imageLoader.load(chosenImageUri)
                    if (imageData != null)
                        recognize( imageData, chosenImageUri)
                }
            }
        }
    }

    @RequiresApi(value = Build.VERSION_CODES.O)
    fun recognize(imageData: ByteArray, imageUri: Uri) {
        viewModelScope.launch(context = Dispatchers.IO) {
            val response = cowNoseRepository.detectAndSearch(file = imageData)
                .transform { it.toDomain(id = -1, imageFilepath = null) }

            when (response) {
                is RequestResult.Success ->
                    _uiState.update {
                        UiState.NoseFound(
                            nose = response.data,
                            imageUri = imageUri,
                            showSavedMessage = false
                        )
                    }

                is RequestResult.Failure ->
                    _uiState.update {
                        UiState.NoseNotFound(
                            imageUri = imageUri,
                            showSavedMessage = false
                        )
                    }
            }
        }
    }

    @RequiresApi(value = Build.VERSION_CODES.O)
    fun insertNose(nose: NoseSearchResult, imageUri: Uri) {
        viewModelScope.launch(context = Dispatchers.IO) {
            val filepath = saveBitmapToFile(context = context, imageUri = imageUri)
            localNoseRepository.insertNoseSearchResult(result = nose.copy(imageFilepath = filepath))
        }
    }
}