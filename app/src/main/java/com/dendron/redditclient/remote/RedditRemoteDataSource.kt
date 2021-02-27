package com.dendron.redditclient.remote

import com.dendron.redditclient.data.datasource.RemoteDataSource
import com.dendron.redditclient.domain.ApiResult
import com.dendron.redditclient.domain.model.Post
import com.dendron.redditclient.remote.model.PostResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RedditRemoteDataSource(private val redditApi: RedditApi) : RemoteDataSource {

    override suspend fun getPosts(limit: Int): ApiResult<List<Post>> =
        withContext(Dispatchers.IO) {
            try {
                val response = redditApi.getPost(limit)
                if (response.isSuccessful) {
                    ApiResult.Success(response.body()?.data?.children?.map { it.toDomain() }
                        ?: emptyList())
                } else {
                    ApiResult.Error(response.errorBody()?.toString() ?: "")
                }
            } catch (ex: Exception) {
                ApiResult.Error(ex.message.toString())
            }
        }
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
