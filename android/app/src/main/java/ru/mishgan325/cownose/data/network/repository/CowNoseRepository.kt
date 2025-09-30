package ru.mishgan325.cownose.data.network.repository

import android.net.Uri
import io.ktor.client.request.forms.formData
import io.ktor.client.request.forms.submitFormWithBinaryData
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import ru.mishgan325.cownose.data.network.WebClient
import ru.mishgan325.cownose.data.network.dto.NoseSearchResultDTO
import ru.mishgan325.cownose.data.network.request.RequestResult
import ru.mishgan325.cownose.data.network.request.safeRequestRaw
import javax.inject.Inject

class CowNoseRepository @Inject constructor(webClient: WebClient) {
    private val _chosenImageUri = MutableStateFlow<Uri?>(value = null)
    val chosenImageUri = _chosenImageUri.asStateFlow()

    private val client = webClient.client

    fun onChosenImageUri(imageUri: Uri) {
        _chosenImageUri.value = imageUri
    }

    suspend fun detectAndSearch(file: ByteArray): RequestResult<NoseSearchResultDTO> =
        client.safeRequestRaw {
            client.submitFormWithBinaryData(
                url = "detect-and-search",
                formData = formData {
                    append(key = "file", value = file, Headers.Companion.build {
                        append(name = HttpHeaders.ContentType, value = "image/jpeg")
                        append(
                            name = HttpHeaders.ContentDisposition,
                            value = "filename=\"cow.jpg\""
                        )
                    })
                }
            )
        }

    suspend fun addEmbedding(name: String, file: ByteArray): RequestResult<Unit> =
        client.safeRequestRaw {
            client.submitFormWithBinaryData(
                url = "add-embedding?name=$name",
                formData = formData {
                    append(key = "file", value = file, Headers.build {
                        append(name = HttpHeaders.ContentType, value = "image/jpeg")
                        append(
                            name = HttpHeaders.ContentDisposition,
                            value = "filename=\"cow.jpg\""
                        )
                    })
                }
            )
        }
}