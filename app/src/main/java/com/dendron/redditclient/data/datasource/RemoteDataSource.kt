package com.dendron.redditclient.data.datasource

import com.dendron.redditclient.domain.ApiResult
import com.dendron.redditclient.domain.ResultWrapper
import com.dendron.redditclient.domain.model.Post
import kotlinx.coroutines.flow.Flow

interface RemoteDataSource {

    suspend fun getPosts(limit: Int): ApiResult<List<Post>>

}