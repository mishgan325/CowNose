package ru.mishgan325.cownose.ui.history

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import ru.mishgan325.cownose.domain.entities.NoseSearchResult
import ru.mishgan325.cownose.ui.common.ResultsPanel
import java.time.format.DateTimeFormatter

@Composable
fun NoseSearchResultHistoryItem(
    result: NoseSearchResult,
    onClick: () -> Unit,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showDialog by remember { mutableStateOf(false) }

    ResultsPanel(modifier = modifier.height(IntrinsicSize.Min)) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            // Image on the left
            if (result.imageFilepath != null) {
                AsyncImage(
                    model = "file://${result.imageFilepath}",
                    contentDescription = "Nose Image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(100.dp)
                        .clip(RoundedCornerShape(10.dp))
                )
            } else {
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .aspectRatio(1f),
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Info on the right
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Spacer(modifier = Modifier.height(12.dp))

                if (result.status.equals("success", ignoreCase = true)) {
                    Text(
                        text = "Успех",
                        color = Color(0xff28b128),
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                } else {
                    Text(
                        text = "Провал",
                        color = Color.Red,
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
                Text(
                    text = result.date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")),
                    modifier = Modifier.padding(start = 8.dp)
                )

                // Подробнее > button/text
                TextButton(
                    onClick = onClick,
                    contentPadding = PaddingValues(horizontal = 8.dp)
                ) {
                    Text("Подробнее  >")
                }
            }

            // Delete button with confirmation dialog
            IconButton({
                showDialog = true
            }) {
                Icon(Icons.Outlined.Delete, null)
            }
        }

        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text("Удалить запись?") },
                text = {
                    Column {
                        Text(
                            if (result.status.equals(
                                    "success",
                                    ignoreCase = true
                                )
                            ) "Успех" else "Провал"
                        )
                        Text(result.date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")))
                    }
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            showDialog = false
                            onDeleteClick()
                        }
                    ) {
                        Text("Подтвердить")
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = { showDialog = false }
                    ) {
                        Text("Отмена")
                    }
                }
            )
        }
    }
}