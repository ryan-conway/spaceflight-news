package com.ryanconway.spaceflightnews.domain.repository

import com.ryanconway.spaceflightnews.domain.Result
import com.ryanconway.spaceflightnews.domain.model.Article
import kotlinx.coroutines.flow.Flow

interface ArticleRepository {
    fun fetchArticles(filter: String?): Flow<Result<List<Article>>>
    suspend fun refreshArticles(): Result<Unit>
    suspend fun fetchArticles(startPosition: Int): Result<Unit>
}