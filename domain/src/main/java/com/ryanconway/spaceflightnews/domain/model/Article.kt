package com.ryanconway.spaceflightnews.domain.model

data class Article(
    var id: String,
    var title: String,
    var url: String,
    var imageUrl: String,
    var summary: String,
    var publishedAt: String
)