package ru.mishgan325.cownose.ui.screen.app.history

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.mishgan325.cownose.data.database.entity.NoseSearchResult
import ru.mishgan325.cownose.data.network.repository.LocalNoseRepository
import javax.inject.Inject

@RequiresApi(value = Build.VERSION_CODES.O)
@HiltViewModel
class HistoryViewModel @Inject constructor(private val localNoseRepository: LocalNoseRepository) :
    ViewModel() {
    private val _noseSearchResults = MutableStateFlow<List<NoseSearchResult>>(value = emptyList())
    val noseSearchResults = _noseSearchResults.asStateFlow()

    init {
        viewModelScope.launch(context = Dispatchers.IO) {
            localNoseRepository.getAllNoseSearchResults().collect { value ->
                _noseSearchResults.update { value }
            }
        }
    }

    fun deleteNoseSearchResult(id: Int, imageFilepath: String) {
        viewModelScope.launch {
            localNoseRepository.deleteNoseSearchResult(id = id, imageFilepath = imageFilepath)
        }
    }
}
