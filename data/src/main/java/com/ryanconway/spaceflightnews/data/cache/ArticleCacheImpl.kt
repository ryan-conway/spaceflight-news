package com.ryanconway.spaceflightnews.data.cache

import com.ryanconway.spaceflightnews.data.adapter.ArticleAdapter
import com.ryanconway.spaceflightnews.data.cache.room.SpaceFlightDatabase
import com.ryanconway.spaceflightnews.data.datasource.ArticleCache
import com.ryanconway.spaceflightnews.domain.model.Article
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ArticleCacheImpl @Inject constructor(
    database: SpaceFlightDatabase
): ArticleCache {

    private val articleDao = database.articleDao()

    override fun getArticles(filter: String?): Flow<List<Article>> {
        val filterString = "%${filter ?: ""}%"
        return articleDao.getArticles(filterString).map { list ->
            list.map { ArticleAdapter.toDomain(it) }
        }
    }

    override fun saveArticles(articles: List<Article>) {
        articleDao.saveArticles(articles.map { ArticleAdapter.toEntity(it) })
    }

    override fun clearCache() {
        articleDao.deleteAllArticles()
    }
}