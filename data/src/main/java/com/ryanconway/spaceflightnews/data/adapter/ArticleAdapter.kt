package com.ryanconway.spaceflightnews.data.adapter

import com.ryanconway.spaceflightnews.data.api.retrofit.ApiArticle
import com.ryanconway.spaceflightnews.data.cache.room.ArticleEntity
import com.ryanconway.spaceflightnews.domain.model.Article

object ArticleAdapter {

    fun toDomain(articleEntity: ArticleEntity) = Article(
        id = articleEntity.id,
        title = articleEntity.title,
        url = articleEntity.url,
        imageUrl = articleEntity.imageUrl,
        summary = articleEntity.summary,
        publishedAt = articleEntity.publishedAt,
    )

    fun toDomain(apiArticle: ApiArticle) = Article(
        id = apiArticle.id,
        title = apiArticle.title,
        url = apiArticle.url,
        imageUrl = apiArticle.imageUrl,
        summary = apiArticle.summary,
        publishedAt = apiArticle.publishedAt,
    )

    fun toEntity(article: Article) = ArticleEntity(
        id = article.id,
        title = article.title,
        url = article.url,
        imageUrl = article.imageUrl,
        summary = article.summary,
        publishedAt = article.publishedAt,
    )
}