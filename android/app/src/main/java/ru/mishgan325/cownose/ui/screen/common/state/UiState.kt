package ru.mishgan325.cownose.ui.screen.common.state

import android.net.Uri
import ru.mishgan325.cownose.data.database.entity.NoseSearchResult

sealed class UiState {
    data class Default(val imageUri: Uri?) : UiState()

    data class Error(val message: String) : UiState()

    data object InProgress : UiState()

    data class NoseFound(
        val nose: NoseSearchResult,
        val imageUri: Uri,
        val showSavedMessage: Boolean
    ) : UiState()

    data class NoseNotFound(val imageUri: Uri, val showSavedMessage: Boolean) : UiState()
}