package ru.mishgan325.cownose.ui.screen.common.appbar

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import ru.mishgan325.cownose.R

@OptIn(markerClass = [ExperimentalMaterial3Api::class])
@Composable
fun AppHeader(
    currentScreen: String,
    isProfileScreen: Boolean,
    onNavigateToProfile: () -> Unit,
    onLogout: () -> Unit
) {
    val context = LocalContext.current
    val touchCounter = remember { mutableIntStateOf(value = 0) }

    TopAppBar(
        modifier = Modifier.fillMaxWidth(),
        colors = TopAppBarColors(
            containerColor = MaterialTheme.colorScheme.background,
            scrolledContainerColor = MaterialTheme.colorScheme.background,
            actionIconContentColor = MaterialTheme.colorScheme.onBackground,
            navigationIconContentColor = MaterialTheme.colorScheme.onBackground,
            titleContentColor = MaterialTheme.colorScheme.onBackground,
        ),
        actions = {
            IconButton(
                onClick = {
                    if (isProfileScreen) {
                        if (touchCounter.intValue == 0) {
                            Toast.makeText(
                                context,
                                context.getString(R.string.press_again_to_logout),
                                Toast.LENGTH_SHORT
                            ).show()
                            touchCounter.intValue = 1
                        } else {
                            onLogout()
                            touchCounter.intValue = 0
                        }
                    } else {
                        onNavigateToProfile()
                    }
                }
            ) {
                if (isProfileScreen) Image(
                    imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                    contentDescription = "Log out",
                    colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.onBackground)
                )
                else Image(
                    imageVector = Icons.Default.Person,
                    contentDescription = "User Profile",
                    colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.onBackground)
                )
            }
        },
        title = { Text(text = currentScreen) }
    )
}