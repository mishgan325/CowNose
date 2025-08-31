package ru.mishgan325.cownose

import android.app.Application
import com.google.firebase.FirebaseApp
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin
import ru.mishgan325.cownose.di.koinModule

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        FirebaseApp.initializeApp(this)

        startKoin {
            // androidLogger(Level.DEBUG)
            androidContext(this@MyApplication)
            modules(koinModule)
        }
    }
}