package ru.mishgan325.cownose.data.network.entity

import kotlinx.serialization.Serializable

@Serializable
data class NoseSearchResultDTO(
    val status: String,
    val noseCoordinates: NoseCoordinatesDTO,
    val similarCows: List<SimilarCowDTO>,
    val databaseSize: Int,
    val embeddingSize: Int,
    val searchAlgorithm: String
)