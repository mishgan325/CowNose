package ru.mishgan325.cownose.ui.screen.common.image

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.mishgan325.cownose.data.database.entity.NoseCoordinates

@Composable
fun CroppedImage(
    imageUri: Uri,
    noseCoordinates: NoseCoordinates,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val croppedBitmap = remember { mutableStateOf<Bitmap?>(value = null) }

    LaunchedEffect(key1 = imageUri, key2 = noseCoordinates) {
        val fullBitmap: Bitmap? = withContext(context = Dispatchers.IO) {
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    val src = ImageDecoder.createSource(context.contentResolver, imageUri)
                    ImageDecoder.decodeBitmap(src)
                } else {
                    context.contentResolver.openInputStream(imageUri)?.use { input ->
                        BitmapFactory.decodeStream(input)
                    }
                }
            } catch (_: Throwable) {
                null
            }
        }

        fullBitmap?.let { bm ->
            val cropLeft = noseCoordinates.left.coerceAtLeast(minimumValue = 0)
            val cropTop = noseCoordinates.top.coerceAtLeast(minimumValue = 0)
            val cropWidth = noseCoordinates.width.coerceAtMost(maximumValue = bm.width - cropLeft)
            val cropHeight = noseCoordinates.height.coerceAtMost(maximumValue = bm.height - cropTop)

            croppedBitmap.value = runCatching {
                Bitmap.createBitmap(bm, cropLeft, cropTop, cropWidth, cropHeight)
            }.getOrNull()
        }
    }

    croppedBitmap.value?.let { bmp ->
        Image(
            bitmap = bmp.asImageBitmap(),
            contentDescription = "Cropped nose",
            modifier = modifier
        )
    }
}