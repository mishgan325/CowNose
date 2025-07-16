package ru.mishgan325.cownose

import android.app.Application
import ru.mishgan325.cownose.di.koinModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
//            androidLogger(Level.DEBUG)
            androidContext(this@MyApplication)
            modules(koinModule)
        }
    }
}