package com.dendron.redditclient.data

import com.dendron.redditclient.data.datasource.LocalDataSource
import com.dendron.redditclient.data.datasource.RemoteDataSource
import com.dendron.redditclient.domain.PostRepository
import com.dendron.redditclient.domain.ResultWrapper
import com.dendron.redditclient.domain.model.Post

class PostRepositoryImp(
    private val remoteRemoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource,
    private val isOnlineChecker: IsOnlineChecker
) : PostRepository {
    override suspend fun getPosts(limit: Int): ResultWrapper<List<Post>> {
        if (isOnlineChecker.execute()) {
            when (val result = remoteRemoteDataSource.getPosts(limit)) {
                is ResultWrapper.Error -> ResultWrapper.Error(result.message)
                is ResultWrapper.Success -> {
                    localDataSource.insertAll(result.data)
                }
            }
        }
        return ResultWrapper.Success(localDataSource.getPosts(limit))
    }

    override suspend fun dismissPost(post: Post) {
        localDataSource.delete(post)
    }

}