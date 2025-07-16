package ru.mishgan325.cownose.ui.upload

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun LoadFromGalleryButton(
    onImageLoad: (Uri?) -> Unit,
    modifier: Modifier = Modifier
) {

    val launcherGallery =
        rememberLauncherForActivityResult(
            ActivityResultContracts.GetContent()
        ) { uri: Uri? ->
            if (uri != null)
                onImageLoad(uri)
        }

    Button(
        onClick = {
            launcherGallery.launch("image/*")
        },
        shape = RoundedCornerShape(8.dp),

        modifier = modifier.fillMaxWidth()
    ) {
        Text("Загрузить из галереи", style = MaterialTheme.typography.titleMedium)

    }
}