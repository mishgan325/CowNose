package ru.mishgan325.cownose.data.network.repository

import android.os.Build
import androidx.annotation.RequiresApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import ru.mishgan325.cownose.data.database.dao.NoseSearchResultDao
import ru.mishgan325.cownose.data.database.entity.NoseSearchResult
import ru.mishgan325.cownose.data.database.entity.NoseSearchResultEntity
import ru.mishgan325.cownose.data.database.entity.SimilarCowEntity
import ru.mishgan325.cownose.data.database.entity.toDomain
import java.io.File
import java.time.ZoneOffset
import javax.inject.Inject

class LocalNoseRepository @Inject constructor(private val noseSearchResultDao: NoseSearchResultDao) {
    @RequiresApi(value = Build.VERSION_CODES.O)
    suspend fun insertNoseSearchResult(result: NoseSearchResult) {
        val entity = NoseSearchResultEntity(
            status = result.status,
            imagePath = result.imageFilepath ?: "",
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
        val searchResultId = noseSearchResultDao.insertNoseSearchResult(result = entity).toInt()
        val cowEntities = result.similarCows.map {
            SimilarCowEntity(
                id = 0,
                name = it.name,
                similarity = it.similarity,
                imageUrl = it.imageUrl,
                searchResultId = searchResultId
            )
        }
        noseSearchResultDao.insertSimilarCows(cows = cowEntities)
    }

    @RequiresApi(value = Build.VERSION_CODES.O)
    fun getNoseSearchResult(id: Int): Flow<NoseSearchResult?> =
        noseSearchResultDao.getResultWithSimilarCows(id).map { entity -> entity?.toDomain() }

    @RequiresApi(value = Build.VERSION_CODES.O)
    fun getAllNoseSearchResults(): Flow<List<NoseSearchResult>> =
        noseSearchResultDao.getAllResultsWithSimilarCows()
            .map { list -> list.map { it.toDomain() } }

    suspend fun deleteNoseSearchResult(id: Int, imageFilepath: String?) {
        withContext(context = Dispatchers.IO) {
            noseSearchResultDao.deleteNoseSearchResultById(id)
            noseSearchResultDao.deleteSimilarCowsBySearchResultId(searchResultId = id)
            imageFilepath?.let { path ->
                val file = File(path)
                if (file.exists())
                    file.delete()
            }
        }
    }
}