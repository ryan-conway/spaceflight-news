package com.ryanconway.spaceflightnews.data.datasource

import com.ryanconway.spaceflightnews.domain.model.Article
import kotlinx.coroutines.flow.Flow

interface ArticleCache {
    fun getArticles(filter: String?): Flow<List<Article>>
    fun saveArticles(articles: List<Article>)
    fun clearCache()
}