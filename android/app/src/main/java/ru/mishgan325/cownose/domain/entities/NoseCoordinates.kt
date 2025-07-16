package ru.mishgan325.cownose.domain.entities

import ru.mishgan325.cownose.data.network.entities.NoseCoordinatesDTO
import kotlinx.serialization.Serializable

@Serializable
data class NoseCoordinates(
    val left: Int,
    val top: Int,
    val right: Int,
    val bottom: Int,
    val width: Int,
    val height: Int
)

fun NoseCoordinatesDTO.toDomain(): NoseCoordinates =
    NoseCoordinates(
        left = left,
        top = top,
        right = right,
        bottom = bottom,
        width = width,
        height = height
    )