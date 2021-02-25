package com.dendron.redditclient.local.di

import androidx.room.Room
import com.dendron.redditclient.data.datasource.LocalDataSource
import com.dendron.redditclient.local.AppDatabase
import com.dendron.redditclient.local.LocalDataSourceImp
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val databaseModule = module {
    single {
        Room.databaseBuilder(androidApplication(), AppDatabase::class.java, "data.db").build()
    }
    single<LocalDataSource> { LocalDataSourceImp(get()) }

}