package ru.mishgan325.cownose.ui.screen.common.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.PopUpToBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import androidx.navigation.toRoute
import ru.mishgan325.cownose.ui.screen.app.addembedding.AddEmbeddingScreen
import ru.mishgan325.cownose.ui.screen.app.history.HistoryScreen
import ru.mishgan325.cownose.ui.screen.app.nfc.NFCScreen
import ru.mishgan325.cownose.ui.screen.app.result.ResultsScreen
import ru.mishgan325.cownose.ui.screen.app.search.SearchScreen
import ru.mishgan325.cownose.ui.screen.common.appbar.AppBottom
import ru.mishgan325.cownose.ui.screen.common.appbar.AppHeader
import ru.mishgan325.cownose.ui.screen.separate.historydetails.HistoryDetailsScreen
import ru.mishgan325.cownose.ui.screen.separate.login.LoginScreen
import ru.mishgan325.cownose.ui.screen.separate.profile.ProfileScreen
import ru.mishgan325.cownose.ui.screen.separate.register.RegisterScreen
import ru.mishgan325.cownose.ui.screen.separate.splash.SplashScreen

@RequiresApi(value = Build.VERSION_CODES.O)
@Composable
fun AppNavigation() {
    val navController: NavHostController = rememberNavController()
    val destination = navController.currentBackStackEntryAsState().value?.destination

    val isSeparateScreen = destination?.hierarchy?.any { it.hasRoute<SeparateScreensRoute>() }
    val isProfileScreen = destination?.hierarchy?.any { it.hasRoute<ProfileScreenRoute>() }
    val isDetailsScreen = destination?.hierarchy?.any { it.hasRoute<HistoryDetailsScreenRoute>() }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            if (isSeparateScreen == false || isProfileScreen == true || isDetailsScreen == true) {
                AppHeader(
                    appBarViewModel = hiltViewModel(),
                    currentScreen = AppScreenRoutes().firstOrNull { routeItem ->
                        destination.hasRoute(route = routeItem.route::class)
                    }?.name.orEmpty(),
                    isProfileScreen = isProfileScreen == true,
                    onNavigateToProfile = { navController.navigate(ProfileScreenRoute) },
                    onLogout = {
                        navController.navigate(SeparateScreensRoute) {
                            popUpTo<AppScreensRoute> { inclusive = true }
                            launchSingleTop = true
                        }
                    },
                    onNavigateBack = { navController.navigateUp() },
                    showBack = isProfileScreen == true || isDetailsScreen == true,
                    showProfileAction = isDetailsScreen != true
                )
            }
        },
        bottomBar = {
            if (isSeparateScreen == false || isProfileScreen == true)
                AppBottom(
                    destination = destination,
                    navController = navController
                )
        }
    ) { paddingValues ->
        NavHost(
            modifier = Modifier
                .padding(paddingValues = paddingValues)
                .consumeWindowInsets(paddingValues)
                .padding(all = 16.dp),
            navController = navController,
            startDestination = SeparateScreensRoute
        ) {
            navigation<SeparateScreensRoute>(startDestination = SplashScreenRoute) {
                composable<SplashScreenRoute> {
                    SplashScreen(
                        splashViewModel = hiltViewModel(),
                        onNavigateToSearchScreen = {
                            navController.navigate(AppScreensRoute) {
                                popUpTo<SeparateScreensRoute> { inclusive = true }
                                launchSingleTop = true
                            }
                        },
                        onNavigateToLoginScreen = {
                            navController.navigate(LoginScreenRoute) {
                                popUpTo<SplashScreenRoute> { inclusive = true }
                                launchSingleTop = true
                            }
                        }
                    )
                }
                composable<LoginScreenRoute> {
                    LoginScreen(
                        loginViewModel = hiltViewModel(),
                        onNavigateToSearchScreen = {
                            navController.navigate(AppScreensRoute) {
                                popUpTo<SeparateScreensRoute> { inclusive = true }
                                launchSingleTop = true
                            }
                        },
                        onNavigateToRegisterScreen = { navController.navigate(RegisterScreenRoute) },
                    )
                }
                composable<RegisterScreenRoute> {
                    RegisterScreen(
                        registerViewModel = hiltViewModel(),
                        onNavigateToLoginScreen = {
                            navController.navigate(AppScreensRoute) {
                                popUpTo<SeparateScreensRoute> { inclusive = true }
                                launchSingleTop = true
                            }
                        }
                    )
                }
                composable<ProfileScreenRoute> { ProfileScreen(profileViewModel = hiltViewModel()) }
                composable<HistoryDetailsScreenRoute> {
                    HistoryDetailsScreen(
                        noseSearchResultId = it.toRoute<HistoryDetailsScreenRoute>().id,
                        historyViewModel = hiltViewModel()
                    )
                }
            }

            navigation<AppScreensRoute>(startDestination = SearchScreenRoute) {
                composable<SearchScreenRoute> {
                    SearchScreen(
                        searchViewModel = hiltViewModel(),
                        onNavigateToResults = {
                            navController.navigate(ResultsScreenRoute) { launchSingleTop = true }
                        })
                }
                composable<NFCScreenRoute> { NFCScreen() }
                composable<AddEmbeddingScreenRoute> { AddEmbeddingScreen(addEmbeddingViewModel = hiltViewModel()) }
                composable<ResultsScreenRoute> {
                    ResultsScreen(
                        resultsViewModel = hiltViewModel(),
                        onNavigateToSearch = {
                            navController.navigate(SearchScreenRoute) {
                                NavOptionsBuilder().popUpTo(SearchScreenRoute) {
                                    PopUpToBuilder().inclusive = true
                                }
                                launchSingleTop = true
                            }
                        }
                    )
                }
                composable<HistoryScreenRoute> {
                    HistoryScreen(
                        historyViewModel = hiltViewModel(),
                        onNavigateToDetails = { resultId ->
                            navController.navigate(route = HistoryDetailsScreenRoute(id = resultId)) {
                                launchSingleTop = true
                            }
                        }
                    )
                }
            }
        }
    }
}