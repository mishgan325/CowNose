package ru.mishgan325.cownose.ui.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ru.mishgan325.cownose.data.database.LocalNoseRepository
import ru.mishgan325.cownose.domain.entities.NoseSearchResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


const val TAG = "HistoryViewModel"

class HistoryViewModel(
    private val localNoseRepository: LocalNoseRepository,
//    private val imageLoader: ImageLoader,
) : ViewModel() {

    private val _noseSearchResults = MutableStateFlow<List<NoseSearchResult>>(emptyList())
    val noseSearchResults = _noseSearchResults.asStateFlow()

    init {
        viewModelScope.launch {
            with(Dispatchers.IO) {

                localNoseRepository.getAllNoseSearchResults().collect { value ->
                    _noseSearchResults.update { value }
                }
            }
        }
    }


    fun deleteNoseSearchResult(id: Int, imageFilepath: String) {
        viewModelScope.launch {
            localNoseRepository.deleteNoseSearchResult(id, imageFilepath)
        }
    }
}