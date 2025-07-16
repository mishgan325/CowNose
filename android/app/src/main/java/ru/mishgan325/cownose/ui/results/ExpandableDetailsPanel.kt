package ru.mishgan325.cownose.ui.results

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.mishgan325.cownose.ui.common.ResultsPanel

@Composable
fun ExpandableDetailsPanel(
    databaseSize: Int,
    embeddingSize: Int,
    searchAlgorithm: String,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .animateContentSize()
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = !expanded }
                .padding(vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Info,
                contentDescription = null,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Подробная информация")
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                contentDescription = null
            )
        }
        if (expanded) {
            Spacer(modifier = Modifier.height(4.dp))
            HorizontalDivider(Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(4.dp))
            Column {
                Text(
                    text = "Количество коров в базе: $databaseSize",
                    modifier = Modifier.padding(vertical = 2.dp)
                )
                Text(
                    text = "Размер эмбеддинга: $embeddingSize измерений",
                    modifier = Modifier.padding(vertical = 2.dp)
                )
                Text(
                    text = "Алгоритм поиска: $searchAlgorithm",
                    modifier = Modifier.padding(vertical = 2.dp)
                )
            }
        }
    }
}

@Composable
@Preview(
    backgroundColor = 0xFFF8F4F7, device = "id:pixel_5", showSystemUi = false,
    showBackground = true
)
fun ExpandableDetailsPanelPreview() {
    ResultsPanel {
        ExpandableDetailsPanel(
            databaseSize = 315,
            embeddingSize = 2048,
            searchAlgorithm = "Cosine Similarity"
        )
    }
}
