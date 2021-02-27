package com.dendron.redditclient.local

import com.dendron.redditclient.data.datasource.LocalDataSource
import com.dendron.redditclient.domain.model.Post
import com.dendron.redditclient.domain.model.Status
import com.dendron.redditclient.local.db.AppDatabase
import com.dendron.redditclient.local.model.PostEntity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class LocalDataSourceImp(
    private val appDatabase: AppDatabase,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : LocalDataSource {

    override fun getPosts(limit: Int): Flow<List<Post>> =
        appDatabase.postDao().getAll().map { posts -> posts.map { it.toDomain() } }
            .flowOn(dispatcher)

    override suspend fun insertAll(posts: List<Post>) =
        withContext(dispatcher) {
            appDatabase.postDao().insertAll(posts.map { it.toModel() })
        }

    override suspend fun markPostAsRead(post: Post) {
        withContext(dispatcher) {
            appDatabase.postDao().updateStatus(post.toModel().copy(status = 1))
        }
    }

    override suspend fun dismissPost(post: Post) {
        withContext(dispatcher) {
            appDatabase.postDao().updateStatus(post.toModel().copy(status = 2))
        }
    }

    override suspend fun dismissAll() {
        withContext(dispatcher) {
            appDatabase.postDao().dismissAll()
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
    status = 0
)

private fun PostEntity.toDomain() = Post(
    id = id,
    title = title,
    author = author,
    thumbnail = thumbnail,
    image = image,
    comments = comments,
    created = created,
    status = if (status == 0) Status.unread else Status.read
)