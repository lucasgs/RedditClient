package com.dendron.redditclient.remote

import com.dendron.redditclient.data.datasource.RemoteDataSource
import com.dendron.redditclient.domain.model.Post
import com.dendron.redditclient.remote.model.PostResponse

sealed class ResultWrapper<T> {
    data class Success<T>(private val data: T) : ResultWrapper<T>()
    data class Error<T>(private val message: String) : ResultWrapper<T>()
}

class RedditRemoteDataSource(private val redditApi: RedditApi) : RemoteDataSource {

    override suspend fun getPosts(): ResultWrapper<List<Post>> {

        // TODO: 2/22/21 check internet connection

        val response = redditApi.getPost()
        return if (response.isSuccessful) {
            ResultWrapper.Success(response.body()?.data?.children?.map { it.toDomain() }
                ?: emptyList())
        } else {
            ResultWrapper.Error(response.errorBody()?.toString() ?: "")
        }
    }
}

private fun PostResponse.Data.Children.toDomain() = Post(
    id = data.id,
    title = data.title,
    author = data.author,
    thumbnail = data.thumbnail,
    comments = data.numComments,
    created = data.created
)
