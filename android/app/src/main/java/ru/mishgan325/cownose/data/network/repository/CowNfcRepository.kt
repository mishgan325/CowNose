package ru.mishgan325.cownose.data.network.repository

import jakarta.inject.Inject
import ru.mishgan325.cownose.data.network.BASE_URL
import ru.mishgan325.cownose.data.network.WebClient
import ru.mishgan325.cownose.data.network.dto.CowByNfcResponseDTO
import ru.mishgan325.cownose.data.network.request.RequestResult
import ru.mishgan325.cownose.data.network.request.safeGet

class CowNfcRepository @Inject constructor(webClient: WebClient) {
    private val client = webClient.client

    suspend fun findByNfcUid(nfcUidHex: String): RequestResult<CowByNfcResponseDTO> =
        client.safeGet(urlString = "nfc/$nfcUidHex")
}

fun CowByNfcResponseDTO.fullImageUrlOrNull(): String? {
    val path = imageUrl ?: return null
    return if (path.startsWith("http://") || path.startsWith("https://")) {
        path
    } else {
        BASE_URL.trimEnd('/') + "/" + path.trimStart('/')
    }
}
