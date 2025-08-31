package ru.mishgan325.cownose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.google.firebase.auth.FirebaseAuth
import ru.mishgan325.cownose.ui.CowNoseApp
import ru.mishgan325.cownose.ui.theme.CowNoseTheme
import ru.mishgan325.cownose.data.database.AuthRepository

class MainActivity : ComponentActivity() {
    private val authRepository: AuthRepository by lazy {
        AuthRepository(FirebaseAuth.getInstance())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            CowNoseTheme {
                CowNoseApp(authRepository = authRepository)
            }
        }
    }
}

