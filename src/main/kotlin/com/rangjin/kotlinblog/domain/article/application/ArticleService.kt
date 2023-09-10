package com.rangjin.kotlinblog.domain.article.application

import com.rangjin.kotlinblog.domain.article.dao.ArticleRepository
import com.rangjin.kotlinblog.domain.article.domain.Article
import com.rangjin.kotlinblog.domain.article.dto.request.ArticleCreateOrUpdateRequestDto
import com.rangjin.kotlinblog.domain.article.dto.request.ArticleDeleteRequestDto
import com.rangjin.kotlinblog.domain.article.dto.response.ArticleCreateOrUpdateResponseDto
import com.rangjin.kotlinblog.domain.user.dao.UserRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.lang.Exception

@Service
class ArticleService (
    private val articleRepository: ArticleRepository,

    private val userRepository: UserRepository,
){

    fun create(requestDto: ArticleCreateOrUpdateRequestDto): ArticleCreateOrUpdateResponseDto {
        // todo: email, password 확인 절차 추가
        val user = userRepository.findByEmail(requestDto.email) ?: throw Exception("error")

        // todo: article 유효성 검사("", " ", null)
        val article = Article(requestDto.content, requestDto.title, user)
        return ArticleCreateOrUpdateResponseDto(articleRepository.save(article), requestDto.email)
    }

    fun update(requestDto: ArticleCreateOrUpdateRequestDto, id: Long): ArticleCreateOrUpdateResponseDto {
        // todo: email, password 확인 절차 추가
        // todo: 존재 하지 않는 게시물 예외 처리 정리
        // todo: article 유효성 검사("", " ", null)
        val article = articleRepository.findByIdOrNull(id) ?: throw Exception("error")
        article.update(requestDto.content, requestDto.content)
        return ArticleCreateOrUpdateResponseDto(articleRepository.save(article), requestDto.email)
    }

    fun delete(requestDto: ArticleDeleteRequestDto, articleId: Long) {
        // todo: email, password 확인 절차 추가
        val user = userRepository.findByEmail(requestDto.email)

        val article = articleRepository.findByIdOrNull(articleId) ?: throw Exception("error")

        articleRepository.delete(article)
    }

}