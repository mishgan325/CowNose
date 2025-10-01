package ru.mishgan325.cownose.data.network

import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.ANDROID
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import javax.inject.Inject

internal const val BASE_URL = "http://158.255.0.109:5353/"

class WebClient @Inject constructor(private val tokenProvider: FirebaseIdTokenProvider) {
    val client: HttpClient = HttpClient(engineFactory = Android) {
        expectSuccess = true
        install(plugin = Logging) {
            logger = Logger.ANDROID
            level = LogLevel.INFO
        }
        install(plugin = ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
            })
        }
        install(plugin = Auth) {
            bearer {
                sendWithoutRequest { true }
                loadTokens { tokenProvider.loadTokensOrNull() }
                refreshTokens { tokenProvider.refreshTokensOrNull() }
            }
        }

        defaultRequest {
            url(urlString = BASE_URL)
        }
    }
}