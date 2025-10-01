package ru.mishgan325.cownose.ui.screen.app.nfc

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.mishgan325.cownose.data.network.repository.CowNfcRepository
import ru.mishgan325.cownose.data.network.repository.fullImageUrlOrNull
import ru.mishgan325.cownose.data.network.request.RequestResult
import javax.inject.Inject

@HiltViewModel
class NFCScreenViewModel @Inject constructor(
    private val repository: CowNfcRepository,
    @ApplicationContext private val appContext: Context
) : ViewModel() {

    private val _status = MutableStateFlow("Поднесите телефон к NFC-метке")
    val status = _status.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _cowImageUrl = MutableStateFlow<String?>(null)
    val cowImageUrl = _cowImageUrl.asStateFlow()

    private val _cowName = MutableStateFlow<String?>(null)
    val cowName = _cowName.asStateFlow()

    private val _lastMilkingDate = MutableStateFlow<String?>(null)
    val lastMilkingDate = _lastMilkingDate.asStateFlow()

    private val _cowPen = MutableStateFlow<Int?>(null)
    val cowPen = _cowPen.asStateFlow()

    fun onNfcDetected(nfcUidHex: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _status.value = "Идёт запрос к серверу..."

            when (val res = repository.findByNfcUid(nfcUidHex)) {
                is RequestResult.Success -> {
                    val cow = res.data
                    _status.value = if (!cow.cowName.isNullOrBlank()) {
                        "Найдена корова: ${cow.cowName}"
                    } else {
                        "Корову найдено, имя отсутствует"
                    }

                    _cowName.value = cow.cowName
                    _cowImageUrl.value = cow.fullImageUrlOrNull()
                    _lastMilkingDate.value = cow.lastMilkingDate
                    _cowPen.value = cow.cowPen
                }

                is RequestResult.Failure -> {
                    _status.value = "Ошибка: ${res.error ?: "неизвестная"}"
                    _cowName.value = null
                    _cowImageUrl.value = null
                    _lastMilkingDate.value = null
                    _cowPen.value = null
                }
            }

            _isLoading.value = false
        }
    }
}
