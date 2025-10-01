package ru.mishgan325.cownose.data.database.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "similar_cows",
    foreignKeys = [
        ForeignKey(
            entity = NoseSearchResultEntity::class,
            parentColumns = ["id"],
            childColumns = ["searchResultId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class SimilarCowEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val similarity: Double,
    val imageUrl: String,
    val searchResultId: Int
)

fun SimilarCowEntity.toDomain(): SimilarCow =
    SimilarCow(
        name = name,
        similarity = similarity,
        imageUrl = imageUrl
    )