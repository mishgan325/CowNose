package ru.mishgan325.cownose.di

import android.content.Context
import androidx.room.Room
import com.google.firebase.auth.FirebaseAuth
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
import ru.mishgan325.cownose.data.database.AuthRepository
import ru.mishgan325.cownose.ui.addembedding.AddEmbeddingViewModel
import ru.mishgan325.cownose.ui.login.LoginViewModel
import ru.mishgan325.cownose.ui.register.RegisterViewModel

val koinModule = module {
    singleOf(::WebClient)
    singleOf(::NetworkNoseRepository)
    single { ImageLoader(androidContext()) }

    viewModelOf(::UploadViewModel)
    viewModelOf(::ResultViewModel)
    viewModelOf(::HistoryViewModel)
    viewModelOf(::AddEmbeddingViewModel)

    single { provideDatabase(androidContext()) }
    single { get<AppDatabase>().noseSearchResultDao() }
    singleOf(::LocalNoseRepository)


    single(createdAtStart = false) { FirebaseAuth.getInstance() }
    single { AuthRepository(get()) }

    viewModelOf(::LoginViewModel)
    viewModelOf(::RegisterViewModel)


}

fun provideDatabase(context: Context): AppDatabase =
    Room.databaseBuilder(
        context.applicationContext,
        AppDatabase::class.java,
        "nose_database"
    ).build()