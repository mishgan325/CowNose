package ru.mishgan325.cownose.data.network.entity

import kotlinx.serialization.Serializable

@Serializable
data class SimilarCowDTO(val name: String, val similarity: Double, val imageUrl: String)