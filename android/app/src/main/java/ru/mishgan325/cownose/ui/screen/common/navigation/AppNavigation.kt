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

    val currentScreen = AppScreenRoutes().firstOrNull { routeItem ->
        destination?.hasRoute(route = routeItem.route::class) == true
    }?.name.orEmpty()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            if (isSeparateScreen == false || isProfileScreen == true) {
                AppHeader(
                    currentScreen = currentScreen,
                    isProfileScreen = isProfileScreen == true,
                    onNavigateToProfile = { navController.navigate(ProfileScreenRoute) },
                    onLogout = { navController.navigate(LoginScreenRoute) { popUpTo(id = 0) } }
                )
            }
        },
        bottomBar = {
            if (isSeparateScreen == false || isProfileScreen == true)
                AppBottom(
                    currentScreen = currentScreen,
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
                            navController.navigate(SearchScreenRoute) {
                                NavOptionsBuilder().popUpTo(SplashScreenRoute) {
                                    PopUpToBuilder().inclusive = true
                                }
                            }
                        },
                        onNavigateToLoginScreen = {
                            navController.navigate(LoginScreenRoute) {
                                NavOptionsBuilder().popUpTo(SplashScreenRoute) {
                                    PopUpToBuilder().inclusive = true
                                }
                            }
                        }
                    )
                }
                composable<LoginScreenRoute> {
                    LoginScreen(
                        loginViewModel = hiltViewModel(),
                        onNavigateToRegisterScreen = { navController.navigate(RegisterScreenRoute) },
                        onNavigateToSearchScreen = {
                            navController.navigate(SearchScreenRoute) {
                                NavOptionsBuilder().popUpTo(LoginScreenRoute) {
                                    PopUpToBuilder().inclusive = true
                                }
                            }
                        }
                    )
                }
                composable<RegisterScreenRoute> {
                    RegisterScreen(
                        registerViewModel = hiltViewModel(),
                        onNavigateToLoginScreen = {
                            navController.navigate(LoginScreenRoute) {
                                NavOptionsBuilder().popUpTo(RegisterScreenRoute) {
                                    PopUpToBuilder().inclusive = true
                                }
                            }
                        }
                    )
                }
                composable<ProfileScreenRoute> {
                    ProfileScreen(
                        profileViewModel = hiltViewModel()
                    )
                }
                composable<HistoryDetailsScreenRoute> {
                    val route: HistoryDetailsScreenRoute = it.toRoute()

                    HistoryDetailsScreen(
                        noseSearchResultId = route.id,
                        historyViewModel = hiltViewModel()
                    )
                }
            }

            navigation<AppScreensRoute>(startDestination = SearchScreenRoute) {
                composable<SearchScreenRoute> {
                    SearchScreen(
                        searchViewModel = hiltViewModel(),
                        onNavigateToResults = { navController.navigate(ResultsScreenRoute) }
                    )
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