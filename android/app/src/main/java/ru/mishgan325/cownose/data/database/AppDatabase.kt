package ru.mishgan325.cownose.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import ru.mishgan325.cownose.data.database.dao.NoseSearchResultDao
import ru.mishgan325.cownose.data.database.entity.NoseSearchResultEntity
import ru.mishgan325.cownose.data.database.entity.SimilarCowEntity

@Database(
    entities = [NoseSearchResultEntity::class, SimilarCowEntity::class],
    version = 2
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun noseSearchResultDao(): NoseSearchResultDao

    companion object {
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE nose_search_results ADD COLUMN cowName TEXT")
                database.execSQL("ALTER TABLE nose_search_results ADD COLUMN message TEXT")
                database.execSQL("ALTER TABLE nose_search_results ADD COLUMN isMatch INTEGER")
                database.execSQL("ALTER TABLE nose_search_results ADD COLUMN similarity REAL")
            }
        }
    }
}
