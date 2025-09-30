package ru.mishgan325.cownose.data.network.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NoseCoordinatesDTO(
    @SerialName(value = "left")
    val left: Int,

    @SerialName(value = "top")
    val top: Int,

    @SerialName(value = "right")
    val right: Int,

    @SerialName(value = "bottom")
    val bottom: Int,

    @SerialName(value = "width")
    val width: Int,

    @SerialName(value = "height")
    val height: Int
)