package com.dendron.redditclient.data

import com.dendron.redditclient.data.datasource.LocalDataSource
import com.dendron.redditclient.data.datasource.RemoteDataSource
import com.dendron.redditclient.domain.ApiResult
import com.dendron.redditclient.domain.PostRepository
import com.dendron.redditclient.domain.ResultWrapper
import com.dendron.redditclient.domain.model.Post
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn

class PostRepositoryImp(
    private val remoteRemoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource,
    private val isOnlineChecker: IsOnlineChecker,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : PostRepository {

    override fun getPosts(): Flow<List<Post>> = localDataSource.getPosts(10).flowOn(dispatcher)

    override suspend fun refreshPosts(limit: Int): ResultWrapper<List<Post>> =
        if (isOnlineChecker.execute()) {
            when (val result = remoteRemoteDataSource.getPosts(limit)) {
                is ApiResult.Error -> ResultWrapper.Error(result.message)
                is ApiResult.Success -> {
                    localDataSource.insertAll(result.data)
                    ResultWrapper.Success
                }
            }
        } else ResultWrapper.Success

    override suspend fun dismissPost(post: Post) {
        localDataSource.delete(post)
    }

    override suspend fun dismissAll() {
        localDataSource.deleteAll()
    }

}