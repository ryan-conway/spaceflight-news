package com.ryanconway.spaceflightnews.data.datasource

import com.ryanconway.spaceflightnews.domain.model.Article
import kotlin.jvm.Throws

interface ArticleApi {
    @Throws(Throwable::class)
    suspend fun fetchArticles(position: Int = 0): List<Article>
}