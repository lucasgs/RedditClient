package com.dendron.redditclient.di

import com.dendron.redditclient.data.IsOnlineChecker
import com.dendron.redditclient.data.IsOnlineCheckerImp
import com.dendron.redditclient.data.PostRepositoryImp
import com.dendron.redditclient.domain.PostRepository
import org.koin.dsl.module

val dataModule = module {
    single<IsOnlineChecker> { IsOnlineCheckerImp() }
    single<PostRepository> { PostRepositoryImp(get(), get(), get()) }
}