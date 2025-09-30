package ru.mishgan325.cownose.data.network.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SimilarCowDTO(
    @SerialName(value = "name")
    val name: String,

    @SerialName(value = "similarity")
    val similarity: Double,

    @SerialName(value = "image_url")
    val imageUrl: String
)