package ru.mishgan325.cownose.data.network.entities

import kotlinx.serialization.Serializable

@Serializable
data class NoseCoordinatesDTO(
    val left: Int,
    val top: Int,
    val right: Int,
    val bottom: Int,
    val width: Int,
    val height: Int
)