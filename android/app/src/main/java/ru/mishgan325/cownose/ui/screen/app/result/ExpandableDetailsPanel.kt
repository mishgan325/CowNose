package ru.mishgan325.cownose.ui.screen.app.result

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ru.mishgan325.cownose.R

@Composable
fun ExpandableDetailsPanel(
    databaseSize: Int,
    embeddingSize: Int,
    searchAlgorithm: String,
    modifier: Modifier = Modifier
) {
    val expanded = remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .animateContentSize()
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded.value = !expanded.value }
                .padding(vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(space = 8.dp)
        ) {
            Icon(imageVector = Icons.Default.Info, contentDescription = null)

            Text(
                text = stringResource(id = R.string.detailed_info),
                style = MaterialTheme.typography.bodyLarge
            )

            Box(modifier = Modifier.weight(weight = 1f))

            Icon(
                imageVector = if (expanded.value) Icons.Default.KeyboardArrowUp
                else Icons.Default.KeyboardArrowDown,
                contentDescription = null
            )
        }

        if (expanded.value) {
            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                thickness = DividerDefaults.Thickness,
                color = MaterialTheme.colorScheme.outlineVariant
            )

            Column(verticalArrangement = Arrangement.spacedBy(space = 4.dp)) {
                Text(
                    text = stringResource(id = R.string.database_size, databaseSize),
                    modifier = Modifier.padding(vertical = 2.dp)
                )
                Text(
                    text = stringResource(id = R.string.embedding_size, embeddingSize),
                    modifier = Modifier.padding(vertical = 2.dp)
                )
                Text(
                    text = stringResource(id = R.string.search_algorithm, searchAlgorithm),
                    modifier = Modifier.padding(vertical = 2.dp)
                )
            }
        }
    }
}