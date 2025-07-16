package ru.mishgan325.cownose.ui.upload

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import coil3.compose.rememberAsyncImagePainter

@Composable
fun ImagePreview(
    imageUri: Uri,
    modifier: Modifier = Modifier
) {
    Image(
        painter = rememberAsyncImagePainter(imageUri, contentScale = ContentScale.Companion.Inside),
        contentDescription = null,
        contentScale = ContentScale.Companion.Inside,
        modifier = modifier
    )
}