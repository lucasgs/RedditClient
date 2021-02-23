package com.dendron.redditclient.remote

import com.dendron.redditclient.data.RemoteDataSource
import com.dendron.redditclient.domain.Post

class RedditRemoteDataSource: RemoteDataSource {
    override suspend fun getPosts(): List<Post> {
        return emptyList()
    }

}