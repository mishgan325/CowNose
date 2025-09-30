package ru.mishgan325.cownose.ui.screen.app.result

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ru.mishgan325.cownose.R

@Composable
fun SaveAndRetryPanel(
    onSaveClick: () -> Unit,
    onTryAgainClick: () -> Unit,
    showSavedMessage: Boolean,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(space = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = { onSaveClick() },
            shape = MaterialTheme.shapes.extraLarge,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = stringResource(id = R.string.save_result),
                style = MaterialTheme.typography.titleMedium
            )
        }

        if (showSavedMessage)
            Text(
                text = stringResource(id = R.string.saved),
                style = MaterialTheme.typography.bodyMedium
            )

        OutlinedButton(
            onClick = { onTryAgainClick() },
            shape = MaterialTheme.shapes.extraLarge,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = stringResource(id = R.string.try_again),
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}