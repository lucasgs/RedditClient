package com.dendron.redditclient.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.dendron.redditclient.local.model.PostEntity

@Database(entities = [PostEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun postDao(): PostDao
}
