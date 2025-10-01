package ru.mishgan325.cownose.data.network

import com.google.firebase.auth.FirebaseAuth
import io.ktor.client.plugins.auth.providers.BearerTokens
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseIdTokenProvider @Inject constructor(private val auth: FirebaseAuth) {
    suspend fun loadTokensOrNull(): BearerTokens? {
        val user = auth.currentUser ?: return null
        val token = user.getIdToken(false).await().token ?: return null
        return BearerTokens(accessToken = token, refreshToken = token)
    }

    suspend fun refreshTokensOrNull(): BearerTokens? {
        val user = auth.currentUser ?: return null
        val token = user.getIdToken(true).await().token ?: return null
        return BearerTokens(accessToken = token, refreshToken = token)
    }
}
