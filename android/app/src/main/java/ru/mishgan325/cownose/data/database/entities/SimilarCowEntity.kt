package ru.mishgan325.cownose.data.database.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import ru.mishgan325.cownose.domain.entities.SimilarCow

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
    val searchResultId: Int // Foreign key reference
)


fun SimilarCowEntity.toDomain(): SimilarCow =
    SimilarCow(
        name = name,
        similarity = similarity,
        imageUrl = imageUrl
    )