package com.ryanconway.spaceflightnews.data.cache.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "article")
data class ArticleEntity(
    @PrimaryKey
    @ColumnInfo(name = "id") var id: String,
    @ColumnInfo(name = "title") var title: String,
    @ColumnInfo(name = "summary") var summary: String,
    @ColumnInfo(name = "url") var url: String,
    @ColumnInfo(name = "image_url") var imageUrl: String,
    @ColumnInfo(name = "published_at") var publishedAt: String
)
