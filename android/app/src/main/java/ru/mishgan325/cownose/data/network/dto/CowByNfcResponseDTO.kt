package ru.mishgan325.cownose.data.network.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CowByNfcResponseDTO(
    @SerialName(value = "status")
    val status: String,
    @SerialName(value = "cow_id")
    val cowId: Int? = null,
    @SerialName(value = "cow_name")
    val cowName: String? = null,
    @SerialName(value = "last_milking_date")
    val lastMilkingDate: String? = null,
    @SerialName(value = "cow_pen")
    val cowPen: Int? = null,
    @SerialName(value = "has_image")
    val hasImage: Boolean = false,
    @SerialName(value = "image_url")
    val imageUrl: String? = null,
    @SerialName(value = "image_info")
    val imageInfo: ImageInfoDTO? = null
)