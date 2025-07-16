package ru.mishgan325.cownose.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.mishgan325.cownose.data.database.entities.NoseSearchResultEntity
import ru.mishgan325.cownose.data.database.entities.SimilarCowEntity

@Database(
    entities = [NoseSearchResultEntity::class, SimilarCowEntity::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun noseSearchResultDao(): NoseSearchResultDao
}