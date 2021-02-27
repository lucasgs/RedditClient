package com.dendron.redditclient.local

import androidx.room.*
import com.dendron.redditclient.local.model.PostEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PostDao {

    @Query("SELECT * from Post")
    fun getAll(): Flow<List<PostEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(posts: List<PostEntity>)

    @Delete
    fun delete(post: PostEntity)

    @Query("DELETE FROM Post")
    fun deleteAll()

}