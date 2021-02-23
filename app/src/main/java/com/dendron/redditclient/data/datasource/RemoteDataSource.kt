package com.dendron.redditclient.data.datasource

import com.dendron.redditclient.remote.ResultWrapper

interface RemoteDataSource {

    suspend fun getPosts(limit: Int): ResultWrapper

}