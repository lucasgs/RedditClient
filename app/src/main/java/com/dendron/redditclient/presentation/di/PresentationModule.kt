package com.dendron.redditclient.presentation.di

import com.dendron.redditclient.presentation.PostViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val presentationModule = module {

    viewModel { PostViewModel(get()) }

}