package ru.mishgan325.cownose.ui.upload

import android.net.Uri
import android.os.Environment
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import java.io.File

@Composable
fun CapturePhotoButton(
    onImageLoad: (Uri?) -> Unit,
    modifier: Modifier = Modifier
) {
    var imageUri by remember { mutableStateOf<Uri?>(null) }


    val context = LocalContext.current
    fun createImageFile(): Uri {
        val file = File(
            context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
            "compose_camera_${System.currentTimeMillis()}.jpg"
        )
        return FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",  // Make sure to set up FileProvider in your manifest
            file
        )
    }


    val launcherCamera =
        rememberLauncherForActivityResult(
            ActivityResultContracts.TakePicture()
        ) { success ->
            if (success)

                onImageLoad(imageUri)
        }


    Button(
        onClick = {
            imageUri = createImageFile()
            launcherCamera.launch(imageUri!!)
        },
        shape = RoundedCornerShape(8.dp),

        modifier = modifier.fillMaxWidth()
    ) {

        Text("Сделать фото", style = MaterialTheme.typography.titleMedium)
    }
}