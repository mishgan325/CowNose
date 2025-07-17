package ru.mishgan325.cownose.ui.results

import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import ru.mishgan325.cownose.domain.entities.NoseCoordinates
import kotlinx.coroutines.Dispatchers

@Composable
fun CroppedImage(
    imageUri: Uri,
    noseCoordinates: NoseCoordinates,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var croppedBitmap by remember { mutableStateOf<Bitmap?>(null) }

    LaunchedEffect(imageUri, noseCoordinates) {
        with(Dispatchers.IO) {

            val source = ImageDecoder.createSource(context.contentResolver, imageUri)
            val bitmap =
                ImageDecoder.decodeBitmap(source)


            bitmap.let {
                // Crop using noseCoordinates (ensure values are within bitmap bounds)
                val cropLeft = noseCoordinates.left.coerceAtLeast(0)
                val cropTop = noseCoordinates.top.coerceAtLeast(0)
                val cropWidth = noseCoordinates.width.coerceAtMost(it.width - cropLeft)
                val cropHeight = noseCoordinates.height.coerceAtMost(it.height - cropTop)

                croppedBitmap = Bitmap.createBitmap(it, cropLeft, cropTop, cropWidth, cropHeight)
            }
        }
    }
    if (croppedBitmap != null) {
        Image(
            bitmap = croppedBitmap!!.asImageBitmap(),
            contentDescription = "Cropped nose",
            modifier = modifier
        )
    }
}