package com.dendron.redditclient.local

import com.dendron.redditclient.data.datasource.LocalDataSource
import com.dendron.redditclient.domain.model.Post
import com.dendron.redditclient.local.model.PostEntity

class LocalDataSourceImp(private val appDatabase: AppDatabase) : LocalDataSource {

    override suspend fun getPosts(limit: Int): List<Post> {
        return appDatabase.userDao().getAll().map { it.toDomain() }
    }

    override suspend fun insertAll(posts: List<Post>) {
        appDatabase.userDao().insertAll(posts.map { it.toModel() })
    }

    override suspend fun delete(post: Post) {
        appDatabase.userDao().delete(post.toModel())
    }

    override suspend fun deleteAll() {
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