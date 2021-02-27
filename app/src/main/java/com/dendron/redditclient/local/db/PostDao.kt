package com.dendron.redditclient.local.db

import androidx.room.*
import com.dendron.redditclient.local.model.PostEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PostDao {

    @Query("SELECT * FROM post WHERE status in (0,1)")
    fun getAll(): Flow<List<PostEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAll(posts: List<PostEntity>)

    @Delete
    fun delete(post: PostEntity)

    @Query("DELETE FROM post")
    fun deleteAll()

    @Update
    suspend fun updateStatus(post: PostEntity)

    @Query("UPDATE post set status = 2")
    suspend fun dismissAll()

}