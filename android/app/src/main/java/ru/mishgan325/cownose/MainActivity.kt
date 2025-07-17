package ru.mishgan325.cownose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import ru.mishgan325.cownose.ui.CowNoseApp
import ru.mishgan325.cownose.ui.theme.CowNoseTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            CowNoseTheme {
                CowNoseApp()
            }
        }
    }

}

