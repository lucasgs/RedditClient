package com.dendron.redditclient.data.datasource

import com.dendron.redditclient.domain.model.Post
import kotlinx.coroutines.flow.Flow

interface LocalDataSource {

    fun getPosts(limit: Int): Flow<List<Post>>

    suspend fun insertAll(posts: List<Post>)

    suspend fun markPostAsRead(post: Post)

    suspend fun dismissPost(post: Post)

    suspend fun dismissAll()

}