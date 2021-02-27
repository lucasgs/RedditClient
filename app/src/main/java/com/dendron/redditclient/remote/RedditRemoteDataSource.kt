package com.dendron.redditclient.remote

import com.dendron.redditclient.data.datasource.RemoteDataSource
import com.dendron.redditclient.domain.ApiResult
import com.dendron.redditclient.domain.model.Post
import com.dendron.redditclient.domain.model.Status
import com.dendron.redditclient.remote.model.PostResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RedditRemoteDataSource(private val redditApi: RedditApi) : RemoteDataSource {

    override suspend fun fetchMorePosts(limit: Int, after: String): ApiResult =
        withContext(Dispatchers.IO) {
            try {
                val response = redditApi.fetchMorePost(limit, after)
                if (response.isSuccessful) {
                    val body = response.body()
                    ApiResult.Success(body?.data?.after, body?.data?.children?.map { it.toDomain() }
                        ?: emptyList())
                } else {
                    ApiResult.Error(response.errorBody()?.toString() ?: "")
                }
            } catch (ex: Exception) {
                ApiResult.Error(ex.message.toString())
            }

        }

    private fun PostResponse.Data.Children.toDomain() = Post(
        id = data.id,
        title = data.title,
        author = data.author,
        thumbnail = data.thumbnail,
        image = data.urlOverriddenByDest,
        comments = data.numComments,
        created = data.created,
        status = Status.unread
    )
}
