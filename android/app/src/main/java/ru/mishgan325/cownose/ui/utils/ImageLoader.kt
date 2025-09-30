package ru.mishgan325.cownose.ui.utils

import android.content.Context
import android.net.Uri
import javax.inject.Inject

class ImageLoader @Inject constructor(private val context: Context) {
    fun load(imageUri: Uri): ByteArray? = imageUri.let {
        context.contentResolver.openInputStream(it).use { block -> block?.readBytes() }
    }
}