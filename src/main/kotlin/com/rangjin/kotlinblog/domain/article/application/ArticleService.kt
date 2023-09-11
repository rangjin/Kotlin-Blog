package com.rangjin.kotlinblog.domain.article.application

import com.rangjin.kotlinblog.domain.article.dao.ArticleRepository
import com.rangjin.kotlinblog.domain.article.domain.Article
import com.rangjin.kotlinblog.domain.article.dto.request.ArticleCreateOrUpdateRequestDto
import com.rangjin.kotlinblog.domain.article.dto.request.ArticleDeleteRequestDto
import com.rangjin.kotlinblog.domain.article.dto.response.ArticleCreateOrUpdateResponseDto
import com.rangjin.kotlinblog.domain.user.application.UserService
import com.rangjin.kotlinblog.global.error.CustomException
import com.rangjin.kotlinblog.global.error.ErrorCode
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class ArticleService (

    private val articleRepository: ArticleRepository,

    private val userService: UserService,

){

    fun create(requestDto: ArticleCreateOrUpdateRequestDto): ArticleCreateOrUpdateResponseDto {
        // 사용자 검증
        val user = userService.validateUser(null, requestDto.email!!, requestDto.password!!)

        // 게시물 생성
        val article = Article(requestDto.content!!, requestDto.title!!, user)
        return ArticleCreateOrUpdateResponseDto(articleRepository.save(article), requestDto.email)
    }

    fun update(requestDto: ArticleCreateOrUpdateRequestDto, id: Long): ArticleCreateOrUpdateResponseDto {
        // 존재하지 않는 게시물일 경우
        val article = articleRepository.findByIdOrNull(id) ?: throw CustomException(ErrorCode.ARTICLE_NOT_FOUND)

        // 사용자 검증
        userService.validateUser(article.user, requestDto.email!!, requestDto.password!!)

        article.update(requestDto.content!!, requestDto.title!!)
        return ArticleCreateOrUpdateResponseDto(articleRepository.save(article), requestDto.email)
    }

    fun delete(requestDto: ArticleDeleteRequestDto, id: Long) {
        // 존재하지 않는 게시물일 경우
        val article = articleRepository.findByIdOrNull(id) ?: throw CustomException(ErrorCode.ARTICLE_NOT_FOUND)

        // 사용자 검증
        userService.validateUser(article.user, requestDto.email!!, requestDto.password!!)

        articleRepository.delete(article)
    }

}