package ru.mishgan325.cownose.di

import android.content.Context
import androidx.room.Room
import ru.mishgan325.cownose.data.database.AppDatabase
import ru.mishgan325.cownose.data.database.LocalNoseRepository
import ru.mishgan325.cownose.data.network.NetworkNoseRepository
import ru.mishgan325.cownose.data.network.WebClient
import ru.mishgan325.cownose.ui.history.HistoryViewModel
import ru.mishgan325.cownose.ui.results.ResultViewModel
import ru.mishgan325.cownose.ui.upload.UploadViewModel
import ru.mishgan325.cownose.ui.utlis.ImageLoader
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val koinModule = module {
    singleOf(::WebClient)
    singleOf(::NetworkNoseRepository)
    single { ImageLoader(androidContext()) }

    viewModelOf(::UploadViewModel)
    viewModelOf(::ResultViewModel)
    viewModelOf(::HistoryViewModel)

    single { provideDatabase(androidContext()) }
    single { get<AppDatabase>().noseSearchResultDao() }
    singleOf(::LocalNoseRepository)


}

fun provideDatabase(context: Context): AppDatabase =
    Room.databaseBuilder(
        context.applicationContext,
        AppDatabase::class.java,
        "nose_database"
    ).build()