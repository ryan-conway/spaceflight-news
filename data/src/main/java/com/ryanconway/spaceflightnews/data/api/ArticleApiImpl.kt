package com.ryanconway.spaceflightnews.data.api

import com.ryanconway.spaceflightnews.data.adapter.ArticleAdapter
import com.ryanconway.spaceflightnews.data.api.retrofit.ArticleService
import com.ryanconway.spaceflightnews.data.datasource.ArticleApi
import com.ryanconway.spaceflightnews.domain.model.Article
import javax.inject.Inject

class ArticleApiImpl @Inject constructor(
    private val articleService: ArticleService
) : ArticleApi {

    private val pageSize = 10

    @Throws(Throwable::class)
    override suspend fun fetchArticles(position: Int): List<Article> {
        return articleService.getArticles(position, pageSize)
            .map { ArticleAdapter.toDomain(it) }
    }
}