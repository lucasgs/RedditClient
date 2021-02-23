package com.dendron.redditclient.remote.di

import com.dendron.redditclient.BuildConfig
import com.dendron.redditclient.data.datasource.RemoteDataSource
import com.dendron.redditclient.remote.RedditApi
import com.dendron.redditclient.remote.RedditRemoteDataSource
import okhttp3.OkHttpClient
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val networkModule = module {
    factory { provideOkHttpClient() }
    factory { provideRedditApi(get()) }
    single { provideRetrofit(get()) }
    single<RemoteDataSource> { RedditRemoteDataSource(get()) }
}

fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
    return Retrofit.Builder().baseUrl(BuildConfig.API_URL).client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create()).build()
}

fun provideOkHttpClient(): OkHttpClient {
    return OkHttpClient().newBuilder().build()
}

fun provideRedditApi(retrofit: Retrofit): RedditApi =
    retrofit.create(RedditApi::class.java)
