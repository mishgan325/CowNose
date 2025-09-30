package ru.mishgan325.cownose.ui.screen.app.addembedding

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ru.mishgan325.cownose.R

@Composable
fun AddEmbeddingDialog(onDismiss: () -> Unit, onConfirm: (String) -> Unit) {
    val name = remember { mutableStateOf(value = "") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = stringResource(id = R.string.enter_name_for_nose),
                style = MaterialTheme.typography.titleMedium
            )
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(space = 12.dp)
            ) {
                TextField(
                    value = name.value,
                    onValueChange = { name.value = it },
                    label = { Text(text = stringResource(id = R.string.name)) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { if (name.value.isNotBlank()) onConfirm(name.value.trim()) },
                shape = MaterialTheme.shapes.extraLarge
            ) { Text(text = stringResource(id = R.string.confirm)) }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                shape = MaterialTheme.shapes.extraLarge
            ) { Text(text = stringResource(id = R.string.cancel)) }
        }
    )
}