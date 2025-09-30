package ru.mishgan325.cownose.ui.screen.common.image

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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import ru.mishgan325.cownose.R
import java.io.File

@Composable
fun CapturePhotoButton(onImageLoad: (Uri?) -> Unit, modifier: Modifier) {
    val imageUri = remember { mutableStateOf<Uri?>(value = null) }
    val context = LocalContext.current

    fun createImageFile(): Uri {
        val file = File(
            context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
            "compose_camera_${System.currentTimeMillis()}.jpg"
        )
        return FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
    }

    val launcherCamera =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.TakePicture()) {
            if (it) onImageLoad(imageUri.value)
        }

    Button(
        onClick = {
            imageUri.value = createImageFile()
            launcherCamera.launch(input = imageUri.value!!)
        },
        shape = RoundedCornerShape(size = 8.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            text = stringResource(id = R.string.take_photo),
            style = MaterialTheme.typography.titleMedium
        )
    }
}