package ru.mishgan325.cownose.data.database.entity

import kotlinx.serialization.Serializable
import ru.mishgan325.cownose.data.network.dto.NoseCoordinatesDTO

@Serializable
data class NoseCoordinates(
    val left: Int,
    val top: Int,
    val right: Int,
    val bottom: Int,
    val width: Int,
    val height: Int
)

fun NoseCoordinatesDTO.toDomain(): NoseCoordinates = NoseCoordinates(
    left = left,
    top = top,
    right = right,
    bottom = bottom,
    width = width,
    height = height
)