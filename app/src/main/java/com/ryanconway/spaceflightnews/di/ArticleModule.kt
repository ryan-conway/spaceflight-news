package com.ryanconway.spaceflightnews.di

import com.ryanconway.spaceflightnews.data.api.ArticleApiImpl
import com.ryanconway.spaceflightnews.data.api.retrofit.SpaceFlightApi
import com.ryanconway.spaceflightnews.data.cache.ArticleCacheImpl
import com.ryanconway.spaceflightnews.data.cache.room.SpaceFlightDatabase
import com.ryanconway.spaceflightnews.data.datasource.ArticleApi
import com.ryanconway.spaceflightnews.data.datasource.ArticleCache
import com.ryanconway.spaceflightnews.domain.repository.ArticleRepository
import com.ryanconway.spaceflightnews.data.repository.ArticleRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object ArticleRepositoryModule {

    @Provides
    fun provideArticleCache(database: SpaceFlightDatabase): ArticleCache =
        ArticleCacheImpl(database)

    @Provides
    fun provideArticleApi(): ArticleApi = ArticleApiImpl(SpaceFlightApi.getService())

    @Provides
    fun provideArticleRepository(cache: ArticleCache, api: ArticleApi): ArticleRepository =
        ArticleRepositoryImpl(cache, api)
}