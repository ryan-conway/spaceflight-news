package com.ryanconway.spaceflightnews.data.cache.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ArticleDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveArticles(articleEntity: List<ArticleEntity>)

    @Query("""
        SELECT * FROM article
        WHERE title LIKE :filter
        ORDER BY published_at DESC
    """)
    fun getArticles(filter: String): Flow<List<ArticleEntity>>

    @Query("DELETE FROM article")
    fun deleteAllArticles()
}