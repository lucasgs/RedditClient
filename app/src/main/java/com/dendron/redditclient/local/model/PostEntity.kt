package com.dendron.redditclient.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Post")
data class PostEntity(
    @PrimaryKey val id: String,
    @ColumnInfo val title: String,
    @ColumnInfo val author: String,
    @ColumnInfo val thumbnail: String?,
    @ColumnInfo val comments: Int,
    @ColumnInfo val created: Long,
)
