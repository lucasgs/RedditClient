package com.dendron.redditclient.di

import com.dendron.redditclient.data.PostRepositoryImp
import com.dendron.redditclient.domain.PostRepository
import org.koin.dsl.module

val dataModule = module {
    single<PostRepository> { PostRepositoryImp(get()) }
}