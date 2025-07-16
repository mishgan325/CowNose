package ru.mishgan325.cownose.ui.utlis

import android.content.Context
import android.net.Uri

class ImageLoader(
    private val context: Context
) {
    fun load(imageUri: Uri): ByteArray? {
        return imageUri.let {
            context.contentResolver.openInputStream(it).use {
                it?.readBytes()
            }
        }

    }
}