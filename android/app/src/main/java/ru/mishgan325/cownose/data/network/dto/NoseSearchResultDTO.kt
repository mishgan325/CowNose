package ru.mishgan325.cownose.data.network.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NoseSearchResultDTO(
    @SerialName("status")
    val status: String,

    @SerialName("message")
    val message: String,

    @SerialName("is_match")
    val isMatch: Boolean,

    @SerialName("cow_name")
    val cowName: String? = null,

    @SerialName("similarity")
    val similarity: Double? = null,

    @SerialName("nose_coordinates")
    val noseCoordinates: NoseCoordinatesDTO,

    @SerialName("similar_cows")
    val similarCows: List<SimilarCowDTO>,

    @SerialName("database_size")
    val databaseSize: Int,

    @SerialName("embedding_size")
    val embeddingSize: Int,

    @SerialName("search_algorithm")
    val searchAlgorithm: String
)