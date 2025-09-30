package ru.mishgan325.cownose.ui.screen.common.appbar

import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import ru.mishgan325.cownose.ui.screen.common.navigation.AppIcon
import ru.mishgan325.cownose.ui.screen.common.navigation.AppScreenRoutes

@Composable
fun AppBottom(destination: NavDestination?, navController: NavHostController) {
    NavigationBar {
        AppScreenRoutes().forEach { navItem ->
            NavigationBarItem(
                selected = destination?.hierarchy
                    ?.any { it.hasRoute(route = navItem.route::class) } == true,
                onClick = {
                    navController.navigate(route = navItem.route) {
                        popUpTo(id = navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = { AppIcon(icon = navItem.icon, contentDescription = navItem.name) }
            )
        }
    }
}