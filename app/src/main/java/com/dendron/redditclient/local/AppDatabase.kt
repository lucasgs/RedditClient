package com.dendron.redditclient.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.dendron.redditclient.local.model.PostEntity

@Database(entities = [PostEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): PostDao
}
