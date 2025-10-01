package ru.mishgan325.cownose.ui.screen.separate.splash

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import ru.mishgan325.cownose.R

@Composable
fun SplashScreen(
    splashViewModel: SplashViewModel,
    onNavigateToSearchScreen: () -> Unit,
    onNavigateToLoginScreen: () -> Unit
) {
    val isLoggedIn = splashViewModel.isLoggedIn.collectAsState().value

    val startAnimation = remember { mutableStateOf(value = false) }
    val alphaAnim = animateFloatAsState(
        targetValue = if (startAnimation.value) 1f else 0f,
        animationSpec = tween(durationMillis = 3000)
    )

    LaunchedEffect(key1 = isLoggedIn) {
        startAnimation.value = true
        delay(timeMillis = 4000)

        if (isLoggedIn) onNavigateToSearchScreen()
        else onNavigateToLoginScreen()
    }

    SplashScreenAnimation(alphaAnim = alphaAnim.value)
}

@Composable
fun SplashScreenAnimation(alphaAnim: Float) {
    Box(
        modifier = Modifier
            .background(color = MaterialTheme.colorScheme.background)
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            modifier = Modifier
                .size(size = 200.dp)
                .alpha(alpha = alphaAnim),
            painter = painterResource(id = R.drawable.cow),
            contentDescription = "Logo icon",
            tint = MaterialTheme.colorScheme.primary
        )
    }
}