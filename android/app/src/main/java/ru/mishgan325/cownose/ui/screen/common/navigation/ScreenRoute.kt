package ru.mishgan325.cownose.ui.screen.common.navigation

import androidx.annotation.DrawableRes
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import kotlinx.serialization.Serializable
import ru.mishgan325.cownose.R

sealed interface AppIcon {
    data class Vector(val image: ImageVector) : AppIcon
    data class Res(@param:DrawableRes val resId: Int) : AppIcon
}

data class AppScreenRoute<T : Any>(val name: String, val route: T, val icon: AppIcon)

@Composable
fun AppIcon(icon: AppIcon, contentDescription: String?) {
    when (icon) {
        is AppIcon.Vector -> Icon(
            imageVector = icon.image,
            contentDescription = contentDescription,
            tint = MaterialTheme.colorScheme.onBackground
        )

        is AppIcon.Res -> Icon(
            painter = painterResource(icon.resId),
            contentDescription = contentDescription,
            tint = MaterialTheme.colorScheme.onBackground
        )
    }
}

@Composable
fun AppScreenRoutes(): List<AppScreenRoute<*>> = listOf(
    AppScreenRoute(
        name = stringResource(R.string.nav_search),
        route = SearchScreenRoute,
        icon = AppIcon.Res(resId = R.drawable.search)
    ),
    AppScreenRoute(
        name = stringResource(R.string.nav_nfc),
        route = NFCScreenRoute,
        icon = AppIcon.Res(resId = R.drawable.rfid_line)
    ),
    AppScreenRoute(
        name = stringResource(R.string.nav_add_nose),
        route = AddEmbeddingScreenRoute,
        icon = AppIcon.Res(resId = R.drawable.add)
    ),
    AppScreenRoute(
        name = stringResource(R.string.nav_history),
        route = HistoryScreenRoute,
        icon = AppIcon.Res(resId = R.drawable.history)
    ),
)


@Serializable
data object SeparateScreensRoute

@Serializable
data object SplashScreenRoute

@Serializable
data object LoginScreenRoute

@Serializable
data object RegisterScreenRoute

@Serializable
data class HistoryDetailsScreenRoute(val id: Int = 0)

// App Screens
@Serializable
data object AppScreensRoute

@Serializable
data object ProfileScreenRoute

@Serializable
data object SearchScreenRoute

@Serializable
data object NFCScreenRoute

@Serializable
data object AddEmbeddingScreenRoute

@Serializable
data object ResultsScreenRoute

@Serializable
data object HistoryScreenRoute