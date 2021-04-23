package com.ryanconway.spaceflightnews

import com.ryanconway.spaceflightnews.domain.Result
import com.ryanconway.spaceflightnews.domain.model.Article
import com.ryanconway.spaceflightnews.domain.repository.ArticleRepository
import kotlinx.coroutines.flow.Flow

/**
 * created because mocking refreshArticles return value always returned null
 */
open class MockableArticleRepository: ArticleRepository {

    override fun fetchArticles(filter: String?): Flow<Result<List<Article>>> {
        TODO("Not yet implemented")
    }

    override suspend fun fetchArticles(startPosition: Int): Result<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun refreshArticles(): Result<Unit> {
        TODO("Not yet implemented")
    }
}