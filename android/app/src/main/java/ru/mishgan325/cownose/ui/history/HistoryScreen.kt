package ru.mishgan325.cownose.ui.history

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ru.mishgan325.cownose.ui.common.AppHeader
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun HistoryScreen(
    onNavigateToDetails: (Int) -> Unit, // passes id
    modifier: Modifier = Modifier
) {

    val viewModel: HistoryViewModel = koinViewModel()
    val noseSearchResults by viewModel.noseSearchResults.collectAsStateWithLifecycle()

    Column(modifier = modifier) {

        AppHeader("История", null)

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            contentPadding = PaddingValues(top = 10.dp, end = 12.dp, start = 12.dp)
        ) {
            items(noseSearchResults.reversed()) {
                NoseSearchResultHistoryItem(
                    result = it,
                    onClick = { onNavigateToDetails(it.id) },
                    onDeleteClick = {
                        viewModel.deleteNoseSearchResult(
                            it.id,
                            it.imageFilepath ?: ""
                        )
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

