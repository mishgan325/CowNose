package ru.mishgan325.cownose.data.database.entities

import androidx.room.Embedded
import androidx.room.Relation

data class NoseSearchResultWithSimilarCows(
    @Embedded val result: NoseSearchResultEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "searchResultId"
    )
    val similarCows: List<SimilarCowEntity>
)