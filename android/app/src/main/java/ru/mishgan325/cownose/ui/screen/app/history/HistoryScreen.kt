// HistoryScreen.kt
package ru.mishgan325.cownose.ui.screen.app.history

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import me.saket.swipe.SwipeAction
import me.saket.swipe.SwipeableActionsBox

@RequiresApi(value = Build.VERSION_CODES.O)
@Composable
fun HistoryScreen(historyViewModel: HistoryViewModel, onNavigateToDetails: (Int) -> Unit) {
    val noseSearchResults = historyViewModel.noseSearchResults.collectAsState().value

    Column(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(space = 10.dp),
            contentPadding = PaddingValues(top = 10.dp, start = 12.dp, end = 12.dp, bottom = 10.dp)
        ) {
            items(items = noseSearchResults.asReversed(), key = { it.id }) { item ->
                val showDialog = remember { mutableStateOf(value = false) }

                val deleteAction = SwipeAction(
                    onSwipe = { showDialog.value = true },
                    icon = {
                        Icon(
                            imageVector = Icons.Filled.Delete,
                            contentDescription = "Delete"
                        )
                    },
                    background = MaterialTheme.colorScheme.error
                )

                SwipeableActionsBox(
                    modifier = Modifier.fillMaxWidth(),
                    swipeThreshold = 200.dp,
                    endActions = listOf(deleteAction)
                ) {
                    NoseSearchResultHistoryItem(
                        result = item,
                        onClick = { onNavigateToDetails(item.id) },
                        onDeleteClick = {
                            historyViewModel.deleteNoseSearchResult(
                                id = item.id,
                                imageFilepath = item.imageFilepath ?: ""
                            )
                        },
                        showDialog = showDialog,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}