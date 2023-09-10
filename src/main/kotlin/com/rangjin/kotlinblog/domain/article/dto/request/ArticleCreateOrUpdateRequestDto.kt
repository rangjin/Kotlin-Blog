package com.rangjin.kotlinblog.domain.article.dto.request

class ArticleCreateOrUpdateRequestDto (
    val email: String,

    val password: String,

    val title: String,

    val content: String,
)