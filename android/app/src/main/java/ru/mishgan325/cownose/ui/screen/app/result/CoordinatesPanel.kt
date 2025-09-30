package ru.mishgan325.cownose.ui.screen.app.result

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import ru.mishgan325.cownose.R
import ru.mishgan325.cownose.data.database.entity.NoseCoordinates

@Composable
fun CoordinatesPanel(coordinates: NoseCoordinates, modifier: Modifier = Modifier) {
    val style = MaterialTheme.typography.bodyMedium
    val font = FontFamily.Monospace

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(space = 16.dp)
    ) {
        Column(modifier = Modifier.weight(weight = 1f)) {
            Text(
                text = stringResource(id = R.string.left, coordinates.left),
                style = style,
                fontFamily = font
            )
            Text(
                text = stringResource(id = R.string.top, coordinates.top),
                style = style,
                fontFamily = font
            )
            Text(
                text = stringResource(id = R.string.right, coordinates.right),
                style = style,
                fontFamily = font
            )
            Text(
                text = stringResource(id = R.string.bottom, coordinates.bottom),
                style = style,
                fontFamily = font
            )
        }
        Column(modifier = Modifier.weight(weight = 1f)) {
            Text(
                text = stringResource(id = R.string.width, coordinates.width),
                style = style,
                fontFamily = font
            )
            Text(
                text = stringResource(id = R.string.height, coordinates.height),
                style = style,
                fontFamily = font
            )
        }
    }
}