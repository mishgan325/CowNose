package ru.mishgan325.cownose.data.database.entity

import android.os.Build
import androidx.annotation.RequiresApi
import ru.mishgan325.cownose.data.network.entity.NoseSearchResultDTO
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

@RequiresApi(value = Build.VERSION_CODES.O)
fun NoseSearchResultWithSimilarCows.toDomain(): NoseSearchResult = NoseSearchResult(
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

@RequiresApi(value = Build.VERSION_CODES.O)
fun NoseSearchResultDTO.toDomain(id: Int, imageFilepath: String?): NoseSearchResult =
    NoseSearchResult(
        id = id,
        status = status,
        noseCoordinates = noseCoordinates.toDomain(),
        similarCows = similarCows.map { it.toDomain() },
        databaseSize = databaseSize,
        embeddingSize = embeddingSize,
        searchAlgorithm = searchAlgorithm,
        imageFilepath = imageFilepath,
        date = LocalDateTime.now(),
    )