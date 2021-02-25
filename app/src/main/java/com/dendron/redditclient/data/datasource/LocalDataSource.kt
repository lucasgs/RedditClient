package com.dendron.redditclient.data.datasource

import com.dendron.redditclient.domain.model.Post

interface LocalDataSource {

    suspend fun getPosts(limit: Int): List<Post>

    suspend fun insertAll(posts: List<Post>)

    suspend fun delete(post: Post)

}