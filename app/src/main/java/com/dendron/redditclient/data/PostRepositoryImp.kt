package com.dendron.redditclient.data

import com.dendron.redditclient.data.datasource.RemoteDataSource
import com.dendron.redditclient.domain.PostRepository

class PostRepositoryImp(private val remoteRemoteDataSource: RemoteDataSource): PostRepository{
    override suspend fun getPosts(limit: Int) = remoteRemoteDataSource.getPosts(limit)
}