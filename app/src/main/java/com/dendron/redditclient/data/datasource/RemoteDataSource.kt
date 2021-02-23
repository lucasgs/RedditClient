package com.dendron.redditclient.data.datasource

import com.dendron.redditclient.domain.model.Post
import com.dendron.redditclient.remote.ResultWrapper

interface RemoteDataSource {

    suspend fun getPosts(): ResultWrapper<List<Post>>

}