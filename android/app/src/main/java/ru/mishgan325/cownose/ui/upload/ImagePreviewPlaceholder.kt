package ru.mishgan325.cownose.ui.upload

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
import androidx.compose.ui.unit.dp

@Composable
fun ImagePreviewPlaceholder(

    modifier: Modifier = Modifier
) {


    Box(
        contentAlignment = Alignment.Companion.Center,
        modifier = modifier
            .fillMaxWidth()
            .height(250.dp)
            .border(
                width = 1.dp,
                shape = RoundedCornerShape(16.dp),
                color = Color.Companion.Black
            )
            .clip(androidx.compose.foundation.shape.RoundedCornerShape(16.dp))

    ) {

        Text(
            text = "Загрузите фото",
            style = MaterialTheme.typography.bodyLarge
        )
    }

}
//            Image(
//                painter = rememberAsyncImagePainter(imageUri),
//                contentDescription = null
//            )
