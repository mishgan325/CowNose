package ru.mishgan325.cownose.data.network

import ru.mishgan325.cownose.data.network.entities.NoseSearchResultDTO
import io.ktor.client.request.forms.formData
import io.ktor.client.request.forms.submitFormWithBinaryData
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders

class NetworkNoseRepository(
    val webClient: WebClient
) {
    private val client = webClient.client

    var chosenImageUri: String? = null

    suspend fun detectAndSearch(file: ByteArray): RequestResult<NoseSearchResultDTO> {

        val response: RequestResult<NoseSearchResultDTO> =
            client.safeRequestRaw {

                client.submitFormWithBinaryData(
                    url = "detect-and-search",

                    formData = formData {
                        append("file", file, Headers.Companion.build {
                            append(HttpHeaders.ContentType, "image/jpeg")
                            append(HttpHeaders.ContentDisposition, "filename=\"cow.jpg\"")
                        })
                    }
                )

            }

        return response
    }

}