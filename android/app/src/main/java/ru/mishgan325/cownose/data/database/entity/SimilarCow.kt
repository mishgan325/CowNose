package ru.mishgan325.cownose.data.database.entity

import kotlinx.serialization.Serializable
import ru.mishgan325.cownose.data.network.dto.SimilarCowDTO

@Serializable
data class SimilarCow(
    val name: String,
    val similarity: Double,
    val imageUrl: String
)

fun SimilarCowDTO.toDomain(): SimilarCow = SimilarCow(
    name = name,
    similarity = similarity,
    imageUrl = imageUrl
)

fun SimilarCow.toEntity(searchResultId: Int): SimilarCowEntity = SimilarCowEntity(
    id = 0,
    name = name,
    similarity = similarity,
    imageUrl = imageUrl,
    searchResultId = searchResultId
)