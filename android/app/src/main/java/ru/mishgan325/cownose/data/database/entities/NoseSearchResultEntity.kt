package ru.mishgan325.cownose.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "nose_search_results")
data class NoseSearchResultEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val status: String,
    val date: Long,
    val imagePath: String,
    val left: Int,
    val top: Int,
    val right: Int,
    val bottom: Int,
    val width: Int,
    val height: Int,
    val databaseSize: Int,
    val embeddingSize: Int,
    val searchAlgorithm: String
)


