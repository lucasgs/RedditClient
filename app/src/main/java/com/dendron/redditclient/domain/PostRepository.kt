package com.dendron.redditclient.domain

import com.dendron.redditclient.domain.model.Post
import kotlinx.coroutines.flow.Flow

interface PostRepository {
    fun getPosts(): Flow<List<Post>>
    suspend fun refreshPosts(limit: Int): ResultWrapper<List<Post>>
    suspend fun markPostAsRead(post: Post)
    suspend fun dismissPost(post: Post)
    suspend fun dismissAll()
}