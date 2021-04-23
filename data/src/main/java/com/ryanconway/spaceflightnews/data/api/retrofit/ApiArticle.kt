package com.ryanconway.spaceflightnews.data.api.retrofit

import com.squareup.moshi.Json

data class ApiArticle(
    @Json(name = "id") var id: String,
    @Json(name = "title") var title: String,
    @Json(name = "url") var url: String,
    @Json(name = "imageUrl") var imageUrl: String,
    @Json(name = "summary") var summary: String,
    @Json(name = "publishedAt") var publishedAt: String
)
