package ru.mishgan325.cownose.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import ru.mishgan325.cownose.data.database.entities.NoseSearchResultEntity
import ru.mishgan325.cownose.data.database.entities.NoseSearchResultWithSimilarCows
import ru.mishgan325.cownose.data.database.entities.SimilarCowEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface NoseSearchResultDao {
    @Insert(onConflict = OnConflictStrategy.NONE)
    suspend fun insertNoseSearchResult(result: NoseSearchResultEntity): Long

    @Insert(onConflict = OnConflictStrategy.NONE)
    suspend fun insertSimilarCows(cows: List<SimilarCowEntity>)

    @Transaction
    @Query("SELECT * FROM nose_search_results WHERE id = :id")
    fun getResultWithSimilarCows(id: Int): Flow<NoseSearchResultWithSimilarCows?>

    @Transaction
    @Query("SELECT * FROM nose_search_results")
    fun getAllResultsWithSimilarCows(): Flow<List<NoseSearchResultWithSimilarCows>>

    @Query("DELETE FROM nose_search_results WHERE id = :id")
    suspend fun deleteNoseSearchResultById(id: Int)

    @Query("DELETE FROM similar_cows WHERE searchResultId = :searchResultId")
    suspend fun deleteSimilarCowsBySearchResultId(searchResultId: Int)
}
