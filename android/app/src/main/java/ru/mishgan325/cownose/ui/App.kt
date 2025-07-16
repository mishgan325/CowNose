package ru.mishgan325.cownose.ui

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import ru.mishgan325.cownose.ui.history.HistoryScreen
import ru.mishgan325.cownose.ui.historydetails.HistoryDetailsScreen
import ru.mishgan325.cownose.ui.results.ResultsScreen
import ru.mishgan325.cownose.ui.upload.UploadScreen

fun printBackStack(backStack: List<NavBackStackEntry>) {
    var s = ""
    for (x in backStack) {
        s = s + (" -> " + x.destination.route + (x.arguments?.toString()?.let { " $it" }
            ?: "")) + "\n"
    }
    Log.d("printBackStack: ", s)
}


@SuppressLint("RestrictedApi")
@Composable
fun CowNoseApp(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()


    // is not available without @SuppressLint("RestrictedApi") for some reason
    // TODO check if it's a bug
    val backstack by navController.currentBackStack.collectAsStateWithLifecycle()

//    printBackStack(backstack)
    Scaffold(
        bottomBar = {
            NavigationBar {
                topLevelRoutes.forEachIndexed { index, topLevelRoute ->
                    NavigationBarItem(
                        icon = {
                            Icon(
                                painterResource(topLevelRoute.icon),
                                contentDescription = topLevelRoute.name,
                            )
                        },
                        label = { Text(topLevelRoute.name) },
                        selected = let {
                            for (backStackEntry in backstack.reversed()) {
                                for (route in topLevelRoutes) {
                                    if (backStackEntry.destination.hasRoute(route.route::class)) {
                                        return@let route == topLevelRoute
                                    }
                                }
                            }
                            return@let false
                        },
                        onClick = {


                            if (backStackEntry?.destination?.hasRoute(topLevelRoute.route::class) == false) {

                                navController.navigate(topLevelRoute.route) {

                                    // Pop up to the start destination of the graph to
                                    // avoid building up a large stack of destinations
                                    // on the back stack as users select items
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    // Avoid multiple copies of the same destination when
                                    // reselecting the same item
                                    launchSingleTop = true
                                    // Restore state when reselecting a previously selected item
                                    restoreState = true
                                }
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController,
            startDestination = UploadScreenRoute,
            Modifier
                .padding(innerPadding)
                .consumeWindowInsets(innerPadding)
        ) {
            composable<UploadScreenRoute> {
                UploadScreen(
                    onNavigateToResults = {
                        navController.navigate(ResultsScreenRoute) {
                        }
                    },
                    modifier = Modifier
                )
            }

            composable<HistoryScreenRoute> {
                HistoryScreen(
                    onNavigateToDetails = { id ->
                        navController.navigate(HistoryDetailsRoute(id)) {
                            launchSingleTop = true
                        }
                    },
                    modifier = Modifier
                )
            }

            composable<ResultsScreenRoute> { backStackEntry ->
                ResultsScreen(
                    onNavigateToUpload = {
                        navController.navigate(UploadScreenRoute) {
                            popUpTo(UploadScreenRoute) {
                                inclusive = true
                            }
                            launchSingleTop = true
                        }
                    },
                    onNavigateBack = {
                        navController.popBackStack()
                    },
                    modifier = Modifier
                )
            }

            composable<HistoryDetailsRoute> { backStackEntry ->
                val route: HistoryDetailsRoute = backStackEntry.toRoute()
                HistoryDetailsScreen(
                    noseSearchResultId = route.id,
                    onNavigateBack = {
                        navController.popBackStack()
                    },
                    modifier = Modifier
                )
            }


        }

    }
}