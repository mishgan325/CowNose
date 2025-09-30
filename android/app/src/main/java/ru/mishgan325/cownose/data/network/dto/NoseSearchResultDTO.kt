package ru.mishgan325.cownose.data.network.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NoseSearchResultDTO(
    @SerialName(value = "status")
    val status: String,

    @SerialName(value = "nose_coordinates")
    val noseCoordinates: NoseCoordinatesDTO,

    @SerialName(value = "similar_cows")
    val similarCows: List<SimilarCowDTO>,

    @SerialName(value = "database_size")
    val databaseSize: Int,

    @SerialName(value = "embedding_size")
    val embeddingSize: Int,

    @SerialName(value = "search_algorithm")
    val searchAlgorithm: String
)