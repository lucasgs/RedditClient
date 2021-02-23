package com.dendron.redditclient.di

import com.dendron.redditclient.data.RemoteDataSource
import com.dendron.redditclient.remote.RedditRemoteDataSource
import org.koin.dsl.module

val applicationModule = module {
    single<RemoteDataSource> { RedditRemoteDataSource() }
}