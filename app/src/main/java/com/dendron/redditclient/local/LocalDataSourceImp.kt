package com.dendron.redditclient.local

import com.dendron.redditclient.data.datasource.LocalDataSource
import com.dendron.redditclient.domain.model.Post
import com.dendron.redditclient.local.model.PostEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class LocalDataSourceImp(private val appDatabase: AppDatabase) : LocalDataSource {

    override fun getPosts(limit: Int): Flow<List<Post>> =
        appDatabase.userDao().getAll().map { posts -> posts.map { it.toDomain() } }

    override suspend fun insertAll(posts: List<Post>) =
        withContext(Dispatchers.IO) {
            appDatabase.userDao().insertAll(posts.map { it.toModel() })
        }

    override suspend fun delete(post: Post) =
        withContext(Dispatchers.IO) {
            appDatabase.userDao().delete(post.toModel())
        }

    override suspend fun deleteAll() =
        withContext(Dispatchers.IO) {
            appDatabase.userDao().deleteAll()
        }
}

private fun Post.toModel() = PostEntity(
    id = id,
    title = title,
    author = author,
    thumbnail = thumbnail,
    image = image,
    comments = comments,
    created = created,
)

private fun PostEntity.toDomain() = Post(
    id = id,
    title = title,
    author = author,
    thumbnail = thumbnail,
    image = image,
    comments = comments,
    created = created,
)