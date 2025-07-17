package ru.mishgan325.cownose.ui.utlis

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import android.provider.OpenableColumns
import android.util.Log
import androidx.core.graphics.scale
import androidx.exifinterface.media.ExifInterface
import kotlinx.coroutines.Dispatchers
import org.koin.core.component.KoinComponent
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.util.UUID

const val TAG = "FileUtils"


fun getFilePath(
    context: Context,
    uri: Uri
): String? {
    val returnCursor = context.contentResolver.query(uri, null, null, null, null)
    val nameIndex = returnCursor!!.getColumnIndex(OpenableColumns.DISPLAY_NAME)
    val sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE)
    returnCursor.moveToFirst()
    val name = returnCursor.getString(nameIndex)
    returnCursor.getLong(sizeIndex).toString()
    val file = File(context.filesDir, name)
    try {
        val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
        val outputStream = FileOutputStream(file)
        var read = 0
        val maxBufferSize = 1 * 1024 * 1024
        val bytesAvailable: Int = inputStream?.available() ?: 0
        val bufferSize = minOf(bytesAvailable, maxBufferSize)
        val buffers = ByteArray(bufferSize)
        while (inputStream?.read(buffers).also {
                if (it != null) {
                    read = it
                }
            } != -1) {
            outputStream.write(buffers, 0, read)
        }
        inputStream?.close()
        outputStream.close()
    } catch (_: Exception) {
    } finally {
        returnCursor.close()
    }
    return file.path
}


/**
 * Saves a file with [Uri] to the app's private files directory and returns the absolute file path.
 */
fun saveBitmapToFile(imageUri: Uri, directoryName: String = "images"): String {
    val context: Context = (object : KoinComponent {}).getKoin().get()
    val path = getFilePath(context, imageUri)!!
    val exif: ExifInterface = ExifInterface(path)
    val orientation =
        exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED)
    val width =
        exif.getAttributeInt(ExifInterface.TAG_IMAGE_WIDTH, 0)
    val height =
        exif.getAttributeInt(ExifInterface.TAG_IMAGE_LENGTH, 0)

    val somethingWrong = (orientation == 6) && width > height

    Log.d(
        TAG, "saveBitmapToFile: orientation $orientation, width $width, height $height\n" +
                "problems? $somethingWrong"
    )



    with(Dispatchers.IO) {

        val dir = File(context.filesDir, directoryName)
        if (!dir.exists()) dir.mkdirs()
        val filename = "${UUID.randomUUID()}.jpg"
        val file = File(dir, filename)

        context.contentResolver.openInputStream(imageUri)?.use { inputStream ->
            FileOutputStream(file).use { outputStream ->
                val bitmap = BitmapFactory.decodeStream(inputStream)

                val correctedBitmap = if (somethingWrong) {
                    val matrix: Matrix = Matrix()
                    matrix.postRotate(90f)
                    val scaledBitmap = bitmap.scale(width, height)
                    val rotatedBitmap: Bitmap = Bitmap.createBitmap(
                        scaledBitmap,
                        0,
                        0,
                        scaledBitmap.getWidth(),
                        scaledBitmap.getHeight(),
                        matrix,
                        true
                    )
                    rotatedBitmap
                } else bitmap

                correctedBitmap.compress(Bitmap.CompressFormat.JPEG, 60, outputStream)
            }
        } ?: throw IllegalArgumentException("Cannot open input stream from Uri: $imageUri")

        // ideally this file should not be created but i don't have time to fix
        val oldFile = File(path)
        if (oldFile.exists()) {
            oldFile.delete()
        }

        return file.absolutePath
    }


}

/**
 * Loads a [Bitmap] from the given file path or returns null if not found.
 */
fun loadBitmapFromFile(imagePath: String): Bitmap? =
    BitmapFactory.decodeFile(imagePath)

/**
 * Deletes an image file by its path.
 */
fun deleteImageFile(imagePath: String) {
    val file = File(imagePath)
    if (file.exists()) file.delete()
}