package ru.mishgan325.cownose.domain.entities

import ru.mishgan325.cownose.data.database.entities.SimilarCowEntity
import ru.mishgan325.cownose.data.network.entities.SimilarCowDTO
import kotlinx.serialization.Serializable

@Serializable
data class SimilarCow(
    val name: String,
    val similarity: Double,
    val imageUrl: String
)

fun SimilarCowDTO.toDomain(): SimilarCow =
    SimilarCow(
        name = name,
        similarity = similarity,
        imageUrl = image_url
    )

fun SimilarCow.toEntity(searchResultId: Int): SimilarCowEntity =
    SimilarCowEntity(
        id = 0,
        name = name,
        similarity = similarity,
        imageUrl = imageUrl,
        searchResultId = searchResultId
    )