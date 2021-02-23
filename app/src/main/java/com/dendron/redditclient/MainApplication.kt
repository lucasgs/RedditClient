package com.dendron.redditclient

import android.app.Application
import com.dendron.redditclient.di.applicationModule
import com.dendron.redditclient.remote.di.networkModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class MainApplication: Application(){
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@MainApplication)
            androidLogger()
            modules(applicationModule, networkModule)
        }
    }
}