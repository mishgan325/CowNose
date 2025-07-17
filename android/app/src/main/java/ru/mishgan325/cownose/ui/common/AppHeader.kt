package ru.mishgan325.cownose.ui.common

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun AppHeader(
    title: String,
    onNavigateBack: (() -> Unit)?,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.wrapContentHeight()
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
        ) {
            if (onNavigateBack != null) {
                IconButton(onNavigateBack, modifier = Modifier.align(Alignment.CenterStart)) {
                    Icon(Icons.AutoMirrored.Outlined.ArrowBack, "go back")
                }
            }

            Text(
                title,
                modifier = Modifier.align(Alignment.Center),
                style = MaterialTheme.typography.titleLarge
            )

        }

        Spacer(Modifier.height(6.dp))
        HorizontalDivider(Modifier.fillMaxWidth())

    }
}

@Composable
@Preview(
    backgroundColor = 0xFFF8F4F7, device = "id:pixel_5", showSystemUi = false,
    showBackground = true
)
fun AppHeaderPreview() {
    AppHeader("Подробности", { })
}