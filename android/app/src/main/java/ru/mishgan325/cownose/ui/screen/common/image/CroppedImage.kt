package ru.mishgan325.cownose.ui.screen.common.image

import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import ru.mishgan325.cownose.data.database.entity.NoseCoordinates
import kotlinx.coroutines.Dispatchers

@Composable
fun CroppedImage(
    imageUri: Uri,
    noseCoordinates: NoseCoordinates,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val croppedBitmap = remember { mutableStateOf<Bitmap?>(value = null) }

    LaunchedEffect(imageUri, noseCoordinates) {
        with(Dispatchers.IO) {

            val source = ImageDecoder.createSource(context.contentResolver, imageUri)
            val bitmap = ImageDecoder.decodeBitmap(source)

            bitmap.let {
                val cropLeft = noseCoordinates.left.coerceAtLeast(minimumValue = 0)
                val cropTop = noseCoordinates.top.coerceAtLeast(minimumValue = 0)
                val cropWidth =
                    noseCoordinates.width.coerceAtMost(maximumValue = it.width - cropLeft)
                val cropHeight =
                    noseCoordinates.height.coerceAtMost(maximumValue = it.height - cropTop)

                croppedBitmap.value =
                    Bitmap.createBitmap(it, cropLeft, cropTop, cropWidth, cropHeight)
            }
        }
    }

    if (croppedBitmap.value != null) Image(
        bitmap = croppedBitmap.value!!.asImageBitmap(),
        contentDescription = "Cropped nose",
        modifier = modifier
    )
}