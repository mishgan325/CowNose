package ru.mishgan325.cownose.ui.screen.common.image

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ru.mishgan325.cownose.R

@Composable
fun ImagePreviewPlaceholder(modifier: Modifier) {
    Box(
        contentAlignment = Alignment.Companion.Center,
        modifier = modifier
            .fillMaxWidth()
            .height(height = 250.dp)
            .border(
                width = 1.dp,
                shape = RoundedCornerShape(size = 16.dp),
                color = Color.Companion.Black
            )
            .clip(shape = RoundedCornerShape(size = 16.dp))
    ) {
        Text(
            text = stringResource(id = R.string.upload_photo),
            style = MaterialTheme.typography.bodyLarge
        )
    }
}