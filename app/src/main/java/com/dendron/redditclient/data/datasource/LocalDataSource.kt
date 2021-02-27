package com.dendron.redditclient.data.datasource

import com.dendron.redditclient.domain.model.Post
import kotlinx.coroutines.flow.Flow

interface LocalDataSource {

    fun getPosts(limit: Int): Flow<List<Post>>

    suspend fun insertAll(posts: List<Post>)

    suspend fun delete(post: Post)

    suspend fun deleteAll()

}