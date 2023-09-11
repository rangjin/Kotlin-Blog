package com.rangjin.kotlinblog.domain.comment.application

import com.rangjin.kotlinblog.domain.article.dao.ArticleRepository
import com.rangjin.kotlinblog.domain.comment.dao.CommentRepository
import com.rangjin.kotlinblog.domain.comment.domain.Comment
import com.rangjin.kotlinblog.domain.comment.dto.request.CommentCreateOrUpdateRequestDto
import com.rangjin.kotlinblog.domain.comment.dto.response.CommentCreateOrUpdateResponseDto
import com.rangjin.kotlinblog.domain.comment.dto.request.CommentDeleteRequestDto
import com.rangjin.kotlinblog.domain.user.application.UserService
import com.rangjin.kotlinblog.global.error.CustomException
import com.rangjin.kotlinblog.global.error.ErrorCode
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class CommentService (
    private val commentRepository: CommentRepository,

    private val articleRepository: ArticleRepository,

    private val userService: UserService,
){

    fun create(requestDto: CommentCreateOrUpdateRequestDto, articleId: Long): CommentCreateOrUpdateResponseDto {
        // 사용자 검증
        val user = userService.validateUser(null, requestDto.email!!, requestDto.password!!)

        // 존재하지 않는 게시물일 경우
        val article = articleRepository.findByIdOrNull(articleId) ?: throw CustomException(ErrorCode.ARTICLE_NOT_FOUND)

        return CommentCreateOrUpdateResponseDto(commentRepository.save(Comment(requestDto, article, user)), user.email)
    }

    fun update(requestDto: CommentCreateOrUpdateRequestDto, articleId: Long, commentId: Long): CommentCreateOrUpdateResponseDto {
        // 존재하지 않는 게시물일 경우
        articleRepository.findByIdOrNull(articleId) ?: throw CustomException(ErrorCode.ARTICLE_NOT_FOUND)

        // 존재하지 않는 댓글일 경우
        val comment = commentRepository.findByIdOrNull(commentId) ?: throw CustomException(ErrorCode.COMMENT_NOT_FOUND)

        // 사용자 검증
        userService.validateUser(comment.user, requestDto.email!!, requestDto.password!!)

        comment.update(requestDto)
        return CommentCreateOrUpdateResponseDto(commentRepository.save(comment), requestDto.email)
    }

    fun delete(requestDto: CommentDeleteRequestDto, articleId: Long, commentId: Long) {
        // 존재하지 않는 게시물일 경우
        articleRepository.findByIdOrNull(articleId) ?: throw CustomException(ErrorCode.ARTICLE_NOT_FOUND)

        // 존재하지 않는 댓글일 경우
        val comment = commentRepository.findByIdOrNull(commentId) ?: throw CustomException(ErrorCode.COMMENT_NOT_FOUND)

        // 사용자 검증
        userService.validateUser(comment.user, requestDto.email!!, requestDto.password!!)

        commentRepository.delete(comment)
    }

}