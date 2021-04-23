package com.ryanconway.spaceflightnews.data.cache.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [ArticleEntity::class],
    exportSchema = false,
    version = 1
)
abstract class SpaceFlightDatabase: RoomDatabase() {

    abstract fun articleDao(): ArticleDao
}