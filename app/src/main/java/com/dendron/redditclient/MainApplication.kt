package com.dendron.redditclient

import android.app.Application
import com.dendron.redditclient.di.dataModule
import com.dendron.redditclient.presentation.di.presentationModule
import com.dendron.redditclient.remote.di.networkModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@MainApplication)
            androidLogger()
            modules(networkModule, dataModule, presentationModule)
        }
    }
}