package com.dendron.redditclient.remote

import com.dendron.redditclient.data.datasource.RemoteDataSource
import com.dendron.redditclient.domain.ResultWrapper
import com.dendron.redditclient.domain.model.Post
import com.dendron.redditclient.remote.model.PostResponse

class RedditRemoteDataSource(private val redditApi: RedditApi) : RemoteDataSource {

    override suspend fun getPosts(limit: Int): ResultWrapper<List<Post>> {
        return try {
            val response = redditApi.getPost(limit)
            return if (response.isSuccessful) {
                ResultWrapper.Success(response.body()?.data?.children?.map { it.toDomain() }
                    ?: emptyList())
            } else {
                ResultWrapper.Error(response.errorBody()?.toString() ?: "")
            }
        } catch (ex: Exception) {
            ResultWrapper.Error(ex.message.toString())
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
