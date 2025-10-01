package ru.mishgan325.cownose.data.network.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ImageInfoDTO(
    @SerialName(value = "content_type")
    val contentType: String? = null,
    @SerialName(value = "width")
    val width: Int? = null,
    @SerialName(value = "height")
    val height: Int? = null
)