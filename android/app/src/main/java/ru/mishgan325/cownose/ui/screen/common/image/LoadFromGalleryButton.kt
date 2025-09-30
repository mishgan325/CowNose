package ru.mishgan325.cownose.ui.screen.common.image

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ru.mishgan325.cownose.R

@Composable
fun LoadFromGalleryButton(onImageLoad: (Uri?) -> Unit, modifier: Modifier) {
    val launcherGallery =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) {
            if (it != null)
                onImageLoad(it)
        }

    Button(
        onClick = { launcherGallery.launch("image/*") },
        shape = RoundedCornerShape(size = 8.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            text = stringResource(id = R.string.download_gallery),
            style = MaterialTheme.typography.titleMedium
        )
    }
}