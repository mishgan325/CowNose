package ru.mishgan325.cownose.data.database

import android.util.Log
import ru.mishgan325.cownose.data.database.entities.NoseSearchResultEntity
import ru.mishgan325.cownose.data.database.entities.SimilarCowEntity
import ru.mishgan325.cownose.domain.entities.NoseSearchResult
import ru.mishgan325.cownose.domain.entities.toDomain
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.io.File
import java.time.ZoneOffset

const val TAG = "LocalNoseRepository"
// Repository
class LocalNoseRepository(
    private val dao: NoseSearchResultDao
) {

    suspend fun insertNoseSearchResult(result: NoseSearchResult) {
        val entity = NoseSearchResultEntity(

            status = result.status,
            imagePath = result.imageFilepath ?: let {
                Log.d(
                    TAG,
                    "insertNoseSearchResult: imagePath was null, inserted empty string"
                ); ""
            },
            left = result.noseCoordinates.left,
            top = result.noseCoordinates.top,
            right = result.noseCoordinates.right,
            bottom = result.noseCoordinates.bottom,
            width = result.noseCoordinates.width,
            height = result.noseCoordinates.height,
            databaseSize = result.databaseSize,
            embeddingSize = result.embeddingSize,
            searchAlgorithm = result.searchAlgorithm,
            date = result.date.toEpochSecond(ZoneOffset.UTC)
        )
        val searchResultId = dao.insertNoseSearchResult(entity).toInt()
        val cowEntities = result.similarCows.map {
            SimilarCowEntity(
                id = 0,
                name = it.name,
                similarity = it.similarity,
                imageUrl = it.imageUrl,
                searchResultId = searchResultId
            )
        }
        dao.insertSimilarCows(cowEntities)
    }

    fun getNoseSearchResult(id: Int): Flow<NoseSearchResult?> =
        dao.getResultWithSimilarCows(id).map { entity ->
            entity?.toDomain()
        }

    fun getAllNoseSearchResults(): Flow<List<NoseSearchResult>> =
        dao.getAllResultsWithSimilarCows().map { list ->
            list.map { it.toDomain() }
        }

    suspend fun deleteNoseSearchResult(id: Int, imageFilepath: String?) {
        withContext(Dispatchers.IO) {
            dao.deleteNoseSearchResultById(id)
            dao.deleteSimilarCowsBySearchResultId(id)
            imageFilepath?.let { path ->
                val file = File(path)
                if (file.exists()) {
                    file.delete()
                }
            }
        }
    }
}