package ru.mishgan325.cownose.ui.screen.app.history

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import ru.mishgan325.cownose.R
import ru.mishgan325.cownose.data.database.entity.NoseSearchResult
import ru.mishgan325.cownose.ui.screen.app.result.ResultsPanel
import java.time.format.DateTimeFormatter

@RequiresApi(value = Build.VERSION_CODES.O)
@Composable
fun NoseSearchResultHistoryItem(
    result: NoseSearchResult,
    onClick: () -> Unit,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier,
    showDialog: MutableState<Boolean>
) {
    ResultsPanel(modifier = modifier.height(IntrinsicSize.Min)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(space = 16.dp)
        ) {
            if (result.imageFilepath != null)
                AsyncImage(
                    model = "file://${result.imageFilepath}",
                    contentDescription = "Nose Image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(size = 100.dp)
                        .clip(shape = RoundedCornerShape(size = 10.dp))
                )
            else
                Box(
                    modifier = Modifier
                        .size(size = 100.dp)
                        .aspectRatio(ratio = 1f)
                )

            Column(
                modifier = Modifier
                    .weight(weight = 1f),
                verticalArrangement = Arrangement.spacedBy(space = 8.dp)
            ) {
                val isSuccess = result.status.equals("success", ignoreCase = true)

                Text(
                    text = if (isSuccess) stringResource(id = R.string.success)
                    else stringResource(id = R.string.failure),
                    color = if (isSuccess) Color(color = 0xFF28B128) else MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(start = 8.dp)
                )

                Text(
                    text = result.date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(start = 8.dp)
                )

                TextButton(
                    onClick = { onClick() },
                    contentPadding = PaddingValues(horizontal = 8.dp)
                ) { Text(text = stringResource(id = R.string.details)) }
            }

            IconButton(onClick = { showDialog.value = true }) {
                Icon(imageVector = Icons.Outlined.Delete, contentDescription = null)
            }
        }

        if (showDialog.value)
            AlertDialog(
                onDismissRequest = { showDialog.value = false },
                title = { Text(text = stringResource(id = R.string.delete_record)) },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(space = 6.dp)) {
                        val isSuccess = result.status.equals("success", ignoreCase = true)
                        Text(
                            text = if (isSuccess) stringResource(id = R.string.success)
                            else stringResource(id = R.string.failure)
                        )
                        Text(text = result.date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")))
                    }
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            showDialog.value = false
                            onDeleteClick()
                        }
                    ) { Text(text = stringResource(id = R.string.confirm)) }
                },
                dismissButton = {
                    TextButton(onClick = { showDialog.value = false }) {
                        Text(text = stringResource(id = R.string.cancel))
                    }
                }
            )
    }
}