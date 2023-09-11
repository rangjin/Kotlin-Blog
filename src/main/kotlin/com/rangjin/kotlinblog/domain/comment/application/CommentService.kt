package com.rangjin.kotlinblog.domain.comment.application

import com.rangjin.kotlinblog.domain.article.dao.ArticleRepository
import com.rangjin.kotlinblog.domain.comment.dao.CommentRepository
import com.rangjin.kotlinblog.domain.comment.domain.Comment
import com.rangjin.kotlinblog.domain.comment.dto.request.CommentCreateOrUpdateRequestDto
import com.rangjin.kotlinblog.domain.comment.dto.response.CommentCreateOrUpdateResponseDto
import com.rangjin.kotlinblog.domain.comment.dto.request.CommentDeleteRequestDto
import com.rangjin.kotlinblog.domain.user.application.UserService
import com.rangjin.kotlinblog.domain.user.dao.UserRepository
import com.rangjin.kotlinblog.global.error.CustomException
import com.rangjin.kotlinblog.global.error.ErrorCode
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class CommentService (
    private val commentRepository: CommentRepository,

    private val articleRepository: ArticleRepository,

    private val userRepository: UserRepository,

    private val userService: UserService,

    private val passwordEncoder: PasswordEncoder,
){

    fun create(requestDto: CommentCreateOrUpdateRequestDto, articleId: Long): CommentCreateOrUpdateResponseDto {
        val user = userRepository.findByEmail(requestDto.email!!) ?: throw CustomException(ErrorCode.EMAIL_NOT_FOUND)
        if (!passwordEncoder.matches(requestDto.password, user.password)) throw CustomException(ErrorCode.PASSWORD_MISMATCH)

        val article = articleRepository.findByIdOrNull(articleId) ?: throw CustomException(ErrorCode.ARTICLE_NOT_FOUND)

        return CommentCreateOrUpdateResponseDto(commentRepository.save(Comment(requestDto, article, user)), user.email)
    }

    fun update(requestDto: CommentCreateOrUpdateRequestDto, articleId: Long, commentId: Long): CommentCreateOrUpdateResponseDto {
        articleRepository.findByIdOrNull(articleId) ?: throw CustomException(ErrorCode.ARTICLE_NOT_FOUND)

        val comment = commentRepository.findByIdOrNull(commentId) ?: throw CustomException(ErrorCode.COMMENT_NOT_FOUND)
        userService.validateUser(comment.user, requestDto.email!!, requestDto.password!!)

        comment.update(requestDto)
        return CommentCreateOrUpdateResponseDto(commentRepository.save(comment), requestDto.email)
    }

    fun delete(requestDto: CommentDeleteRequestDto, articleId: Long, commentId: Long) {
        articleRepository.findByIdOrNull(articleId) ?: throw CustomException(ErrorCode.ARTICLE_NOT_FOUND)

        val comment = commentRepository.findByIdOrNull(commentId) ?: throw CustomException(ErrorCode.COMMENT_NOT_FOUND)
        userService.validateUser(comment.user, requestDto.email!!, requestDto.password!!)

        commentRepository.delete(comment)
    }

}