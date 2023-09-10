package com.rangjin.kotlinblog.domain.article.dao

import com.rangjin.kotlinblog.domain.article.domain.Article
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ArticleRepository: JpaRepository<Article, Long>