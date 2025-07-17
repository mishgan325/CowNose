package ru.mishgan325.cownose.ui

import ru.mishgan325.cownose.R
import kotlinx.serialization.Serializable

@Serializable
data class TopLevelRoute<T : Any>(val name: String, val route: T, val icon: Int)

val topLevelRoutes = listOf(
    TopLevelRoute("Главная", UploadScreenRoute, R.drawable.home_24px),
    TopLevelRoute("История", HistoryScreenRoute, R.drawable.history_24px)
)


@Serializable
data object UploadScreenRoute

@Serializable
data object ResultsScreenRoute

@Serializable
data object HistoryScreenRoute

@Serializable
data class HistoryDetailsRoute(val id: Int = 0)


