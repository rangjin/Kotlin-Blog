package com.rangjin.kotlinblog.domain.article.application

import com.rangjin.kotlinblog.domain.article.dao.ArticleRepository
import com.rangjin.kotlinblog.domain.article.domain.Article
import com.rangjin.kotlinblog.domain.article.dto.request.ArticleCreateOrUpdateRequestDto
import com.rangjin.kotlinblog.domain.article.dto.request.ArticleDeleteRequestDto
import com.rangjin.kotlinblog.domain.article.dto.response.ArticleCreateOrUpdateResponseDto
import com.rangjin.kotlinblog.domain.user.application.UserService
import com.rangjin.kotlinblog.domain.user.dao.UserRepository
import com.rangjin.kotlinblog.domain.user.domain.User
import com.rangjin.kotlinblog.global.error.CustomException
import com.rangjin.kotlinblog.global.error.ErrorCode
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class ArticleService (
    private val articleRepository: ArticleRepository,

    private val userRepository: UserRepository,

    private val userService: UserService,

    private val passwordEncoder: PasswordEncoder,
){

    fun create(requestDto: ArticleCreateOrUpdateRequestDto): ArticleCreateOrUpdateResponseDto {
        val user = userRepository.findByEmail(requestDto.email!!) ?: throw CustomException(ErrorCode.EMAIL_NOT_FOUND)
        if (!passwordEncoder.matches(requestDto.password, user.password)) throw CustomException(ErrorCode.PASSWORD_MISMATCH)

        val article = Article(requestDto.content!!, requestDto.title!!, user)
        return ArticleCreateOrUpdateResponseDto(articleRepository.save(article), requestDto.email)
    }

    fun update(requestDto: ArticleCreateOrUpdateRequestDto, id: Long): ArticleCreateOrUpdateResponseDto {
        val article = articleRepository.findByIdOrNull(id) ?: throw CustomException(ErrorCode.ARTICLE_NOT_FOUND)
        userService.validateUser(article.user, requestDto.email!!, requestDto.password!!)

        article.update(requestDto.content!!, requestDto.title!!)

        return ArticleCreateOrUpdateResponseDto(articleRepository.save(article), requestDto.email)
    }

    fun delete(requestDto: ArticleDeleteRequestDto, id: Long) {
        val article = articleRepository.findByIdOrNull(id) ?: throw CustomException(ErrorCode.ARTICLE_NOT_FOUND)
        userService.validateUser(article.user, requestDto.email!!, requestDto.password!!)

        articleRepository.delete(article)
    }

}