package ru.mishgan325.cownose.di

import android.content.Context
import androidx.room.Room
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ru.mishgan325.cownose.data.database.AppDatabase
import ru.mishgan325.cownose.data.database.dao.NoseSearchResultDao
import ru.mishgan325.cownose.data.network.WebClient
import ru.mishgan325.cownose.data.network.repository.AuthRepository
import ru.mishgan325.cownose.data.network.repository.CowNoseRepository
import ru.mishgan325.cownose.data.network.repository.LocalNoseRepository
import ru.mishgan325.cownose.ui.utils.ImageLoader
import javax.inject.Singleton

@Module
@InstallIn(value = [SingletonComponent::class])
object MyModule {
    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun provideWebClient(): WebClient = WebClient()

    @Provides
    @Singleton
    fun provideAuthRepository(firebaseAuth: FirebaseAuth): AuthRepository =
        AuthRepository(firebaseAuth = firebaseAuth)

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "app_database"
        )
            .addMigrations(AppDatabase.MIGRATION_1_2)
            .build()

    @Provides
    @Singleton
    fun provideNoseSearchResultDao(appDatabase: AppDatabase): NoseSearchResultDao =
        appDatabase.noseSearchResultDao()

    @Provides
    @Singleton
    fun provideCowNoseRepository(webClient: WebClient): CowNoseRepository =
        CowNoseRepository(webClient = webClient)

    @Provides
    @Singleton
    fun provideLocalNoseRepository(noseSearchResultDao: NoseSearchResultDao) =
        LocalNoseRepository(noseSearchResultDao = noseSearchResultDao)

    @Provides
    @Singleton
    fun provideImageLoader(@ApplicationContext context: Context): ImageLoader =
        ImageLoader(context = context)
}