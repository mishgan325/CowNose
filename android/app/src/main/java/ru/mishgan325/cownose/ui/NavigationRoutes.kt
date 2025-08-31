package ru.mishgan325.cownose.ui

import ru.mishgan325.cownose.R
import kotlinx.serialization.Serializable

@Serializable
data class TopLevelRoute<T : Any>(val name: String, val route: T, val icon: Int)

val topLevelRoutes = listOf(
    TopLevelRoute("Поиск", UploadScreenRoute, R.drawable.search),
    TopLevelRoute("NFC", NFCScreenRoute, R.drawable.rfid_line),
    TopLevelRoute("Добавить нос", AddEmbeddingScreenRoute, R.drawable.add),
    TopLevelRoute("История", HistoryScreenRoute, R.drawable.history)
)

@Serializable
data object LoginScreenRoute {
    const val route = "login"
}

@Serializable
data object UploadScreenRoute

@Serializable
data object NFCScreenRoute

@Serializable
data object AddEmbeddingScreenRoute

@Serializable
data object ResultsScreenRoute

@Serializable
data object HistoryScreenRoute

@Serializable
data class HistoryDetailsRoute(val id: Int = 0)


