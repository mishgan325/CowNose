package ru.mishgan325.cownose.data.network.entities

import kotlinx.serialization.Serializable

@Serializable
data class SimilarCowDTO(
    val name: String,
    val similarity: Double,
    val image_url: String
)