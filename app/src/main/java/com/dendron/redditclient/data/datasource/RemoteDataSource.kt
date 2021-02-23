package com.dendron.redditclient.data.datasource

import com.dendron.redditclient.domain.ResultWrapper
import com.dendron.redditclient.domain.model.Post

interface RemoteDataSource {

    suspend fun getPosts(limit: Int): ResultWrapper<List<Post>>

}