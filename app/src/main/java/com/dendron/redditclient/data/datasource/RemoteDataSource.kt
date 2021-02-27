package com.dendron.redditclient.data.datasource

import com.dendron.redditclient.domain.ApiResult

interface RemoteDataSource {
    suspend fun fetchMorePosts(limit: Int, after: String): ApiResult
}