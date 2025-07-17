package ru.mishgan325.cownose.data.network

import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.ANDROID
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

internal const val BASE_URL = "http://158.255.0.109:5353/"

class WebClient(
) {
    val client: HttpClient = HttpClient(Android) {
        expectSuccess = true
        install(Logging) {
            logger = Logger.ANDROID
            level = LogLevel.INFO
        }
        install(ContentNegotiation) {

            json(Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
            })
        }
        defaultRequest {
            url(BASE_URL)
        }

    }


    init {
    }
}

