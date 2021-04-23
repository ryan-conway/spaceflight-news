package com.ryanconway.spaceflightnews.data.api.retrofit

import retrofit2.http.GET
import retrofit2.http.Query

interface ArticleService {

    @GET("api/v2/articles")
    suspend fun getArticles(
        @Query("_start") start: Int,
        @Query("_limit") limit: Int
    ): List<ApiArticle>
}