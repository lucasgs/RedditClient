package com.dendron.redditclient.remote

import com.dendron.redditclient.data.datasource.RemoteDataSource
import com.dendron.redditclient.domain.ResultWrapper
import com.dendron.redditclient.domain.model.Post
import com.dendron.redditclient.remote.model.PostResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class RedditRemoteDataSource(private val redditApi: RedditApi) : RemoteDataSource {

    override suspend fun getPosts(limit: Int): Flow<ResultWrapper<List<Post>>> = flow {
        try {
            val response = redditApi.getPost(limit)
            if (response.isSuccessful) {
                emit(ResultWrapper.Success(response.body()?.data?.children?.map { it.toDomain() }
                    ?: emptyList()))
            } else {
                emit(ResultWrapper.Error(response.errorBody()?.toString() ?: ""))
            }
        } catch (ex: Exception) {
            emit(ResultWrapper.Error(ex.message.toString()))
        }
    }.flowOn(Dispatchers.IO)
}

private fun PostResponse.Data.Children.toDomain() = Post(
    id = data.id,
    title = data.title,
    author = data.author,
    thumbnail = data.thumbnail,
    image = data.urlOverriddenByDest,
    comments = data.numComments,
    created = data.created
)
