package ru.mishgan325.cownose.domain.entities

import ru.mishgan325.cownose.data.database.entities.NoseSearchResultWithSimilarCows
import ru.mishgan325.cownose.data.network.entities.NoseSearchResultDTO
import java.time.LocalDateTime
import java.time.ZoneOffset

data class NoseSearchResult(
    val id: Int,
    val date: LocalDateTime,
    val status: String,
    val noseCoordinates: NoseCoordinates,
    val similarCows: List<SimilarCow>,
    val databaseSize: Int,
    val embeddingSize: Int,
    val searchAlgorithm: String,
    val imageFilepath: String?,
)


fun NoseSearchResultWithSimilarCows.toDomain(): NoseSearchResult =
    NoseSearchResult(
        id = result.id,
        status = result.status,
        noseCoordinates = NoseCoordinates(
            left = result.left,
            top = result.top,
            right = result.right,
            bottom = result.bottom,
            width = result.width,
            height = result.height
        ),
        similarCows = similarCows.map {
            SimilarCow(
                name = it.name,
                similarity = it.similarity,
                imageUrl = it.imageUrl
            )
        },
        databaseSize = result.databaseSize,
        embeddingSize = result.embeddingSize,
        searchAlgorithm = result.searchAlgorithm,
        imageFilepath = result.imagePath,
        date = LocalDateTime.ofEpochSecond(result.date, 0, ZoneOffset.UTC)
    )


fun NoseSearchResultDTO.toDomain(id: Int, imageFilepath: String?): NoseSearchResult =
    NoseSearchResult(
        id = id,
        status = status,
        noseCoordinates = nose_coordinates.toDomain(),
        similarCows = similar_cows.map { it.toDomain() },
        databaseSize = database_size,
        embeddingSize = embedding_size,
        searchAlgorithm = search_algorithm,
        imageFilepath = imageFilepath,
        date = LocalDateTime.now(),
    )
