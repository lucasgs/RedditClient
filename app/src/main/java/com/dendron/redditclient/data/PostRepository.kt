package com.dendron.redditclient.data

class PostRepository(private val remoteRemoteDataSource: RemoteDataSource) {
    suspend fun getPosts() = remoteRemoteDataSource.getPosts()
}