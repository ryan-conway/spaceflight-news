package com.ryanconway.spaceflightnews.data

import com.ryanconway.spaceflightnews.androidtestutil.DummyData
import com.ryanconway.spaceflightnews.data.datasource.ArticleApi
import com.ryanconway.spaceflightnews.domain.model.Article

class ArticleApiFake: ArticleApi {

    val articles = DummyData.articles
    val pageSize = 5

    override suspend fun fetchArticles(position: Int): List<Article> {
        return articles.subList(position, position + pageSize)
    }
}