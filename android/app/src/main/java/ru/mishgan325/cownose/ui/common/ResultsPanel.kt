package ru.mishgan325.cownose.ui.common

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ResultsPanel(
    modifier: Modifier = Modifier,
    block: @Composable () -> Unit,
) {
    Surface(
        modifier = modifier

            .border(
                width = 1.dp,
                shape = RoundedCornerShape(10.dp),
                color = Color.Companion.Gray
            )
            .padding(10.dp)

    ) {
        block()
    }
}