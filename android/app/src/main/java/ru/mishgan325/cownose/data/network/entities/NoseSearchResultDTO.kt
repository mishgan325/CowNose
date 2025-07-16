package ru.mishgan325.cownose.data.network.entities

import kotlinx.serialization.Serializable

@Serializable
data class NoseSearchResultDTO(
    val status: String,
    val nose_coordinates: NoseCoordinatesDTO,
    val similar_cows: List<SimilarCowDTO>,
    val database_size: Int,
    val embedding_size: Int,
    val search_algorithm: String
)

