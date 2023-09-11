package com.rangjin.kotlinblog.domain.article.dto.response

import com.rangjin.kotlinblog.domain.article.domain.Article

class ArticleCreateOrUpdateResponseDto(

    val articleId: Long,

    val email: String,

    val title: String,

    val content: String,

) {

    constructor(article: Article, email: String): this(article.id!!, email, article.title, article.content)

}