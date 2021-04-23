package com.ryanconway.spaceflightnews.data.repository

import com.ryanconway.spaceflightnews.domain.Result
import com.ryanconway.spaceflightnews.data.datasource.ArticleApi
import com.ryanconway.spaceflightnews.data.datasource.ArticleCache
import com.ryanconway.spaceflightnews.domain.model.Article
import com.ryanconway.spaceflightnews.domain.repository.ArticleRepository
import kotlinx.coroutines.flow.*
import java.lang.Exception
import javax.inject.Inject

class ArticleRepositoryImpl @Inject constructor(
    private val cache: ArticleCache,
    private val api: ArticleApi
) : ArticleRepository {

    override fun fetchArticles(filter: String?): Flow<Result<List<Article>>> {
        return cache.getArticles(filter)
            .map { Result.Success(it) }
    }

    override suspend fun fetchArticles(startPosition: Int): Result<Unit> {
        val articles = try {
            api.fetchArticles(startPosition)
        } catch (e: Exception) {
            return Result.Failure(e)
        }
        cache.saveArticles(articles)
        return Result.Success()
    }

    override suspend fun refreshArticles(): Result<Unit> {
        val articles = try {
            api.fetchArticles()
        } catch (e: Exception) {
            return Result.Failure(e)
        }
        cache.clearCache()
        cache.saveArticles(articles)
        return Result.Success()
    }
}