package ru.mishgan325.cownose.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.mishgan325.cownose.data.database.dao.NoseSearchResultDao
import ru.mishgan325.cownose.data.database.entity.NoseSearchResultEntity
import ru.mishgan325.cownose.data.database.entity.SimilarCowEntity

@Database(entities = [NoseSearchResultEntity::class, SimilarCowEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun noseSearchResultDao(): NoseSearchResultDao
}