package ru.mishgan325.cownose.data.network.repository

import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepository @Inject constructor(private val firebaseAuth: FirebaseAuth) {
    private val _isLoggedId = MutableStateFlow(value = firebaseAuth.currentUser != null)
    val isLoggedIn = _isLoggedId.asStateFlow()

    private val authStateListener = FirebaseAuth.AuthStateListener { auth ->
        _isLoggedId.value = auth.currentUser != null
    }

    init {
        firebaseAuth.addAuthStateListener(authStateListener)
    }

    suspend fun login(email: String, password: String): Result<Unit> =
        try {
            firebaseAuth.signInWithEmailAndPassword(email, password).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }

    suspend fun register(email: String, password: String): Result<Unit> =
        try {
            firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }

    fun logout() = firebaseAuth.signOut()

    fun cleanUp() = firebaseAuth.removeAuthStateListener(authStateListener)
}