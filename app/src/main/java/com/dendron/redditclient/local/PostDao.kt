package com.dendron.redditclient.local

import androidx.room.*
import com.dendron.redditclient.local.model.PostEntity

@Dao
interface PostDao {

    @Query("SELECT * from Post")
    suspend fun getAll(): List<PostEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(posts: List<PostEntity>)

    @Delete
    fun delete(post: PostEntity)

}