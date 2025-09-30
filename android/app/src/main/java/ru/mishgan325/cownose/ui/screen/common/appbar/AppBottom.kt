package ru.mishgan325.cownose.ui.screen.common.appbar

import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import ru.mishgan325.cownose.ui.screen.common.navigation.AppIcon
import ru.mishgan325.cownose.ui.screen.common.navigation.AppScreenRoutes

@Composable
fun AppBottom(currentScreen: String, navController: NavHostController) {
    NavigationBar {
        AppScreenRoutes().forEachIndexed { index, navItem ->
            NavigationBarItem(
                selected = currentScreen == navItem.route,
                onClick = { navController.navigate(route = navItem.route) },
                icon = {
                    AppIcon(
                        icon = navItem.icon,
                        contentDescription = navItem.name
                    )
                }
            )
        }
    }
}