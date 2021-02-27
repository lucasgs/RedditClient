package com.dendron.redditclient.local

import com.dendron.redditclient.data.datasource.LocalDataSource
import com.dendron.redditclient.domain.model.Post
import com.dendron.redditclient.local.model.PostEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LocalDataSourceImp(private val appDatabase: AppDatabase) : LocalDataSource {

    override suspend fun getPosts(limit: Int): List<Post> {
        return appDatabase.userDao().getAll().map { it.toDomain() }
    }

    override suspend fun insertAll(posts: List<Post>) {
        withContext(Dispatchers.IO) {
            appDatabase.userDao().insertAll(posts.map { it.toModel() })
        }
    }

    override suspend fun delete(post: Post) {
        withContext(Dispatchers.IO) {
            appDatabase.userDao().delete(post.toModel())
        }
    }

    override suspend fun deleteAll() {
        withContext(Dispatchers.IO) {
            appDatabase.userDao().deleteAll()
        }
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