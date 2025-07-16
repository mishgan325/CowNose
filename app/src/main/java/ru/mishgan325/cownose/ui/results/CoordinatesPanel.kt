package ru.mishgan325.cownose.ui.results

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import ru.mishgan325.cownose.domain.entities.NoseCoordinates

@Composable
fun CoordinatesPanel(
    coordinates: NoseCoordinates,
    modifier: Modifier = Modifier
) {

    val style = MaterialTheme.typography.bodyMedium
    val font = FontFamily.Monospace

    Row(
        modifier,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {


        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                "left:   ${coordinates.left} px",
                style = style,
                fontFamily = font
            )
            Text(
                "top:    ${coordinates.top} px",
                style = style,
                fontFamily = font
            )
            Text(
                "right:  ${coordinates.right} px",
                style = style,
                fontFamily = font
            )
            Text(
                "bottom: ${coordinates.bottom} px",
                style = style,
                fontFamily = font
            )

        }
        Spacer(Modifier.width(16.dp))
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                "width:  ${coordinates.width} px",
                style = style,
                fontFamily = font
            )
            Text(
                "height: ${coordinates.height} px",
                style = style,
                fontFamily = font
            )

        }
    }

}