package com.rangjin.kotlinblog.domain.comment.application

import com.rangjin.kotlinblog.domain.article.dao.ArticleRepository
import com.rangjin.kotlinblog.domain.comment.dao.CommentRepository
import com.rangjin.kotlinblog.domain.comment.domain.Comment
import com.rangjin.kotlinblog.domain.comment.dto.request.CommentCreateOrUpdateRequestDto
import com.rangjin.kotlinblog.domain.comment.dto.response.CommentCreateOrUpdateResponseDto
import com.rangjin.kotlinblog.domain.comment.dto.request.CommentDeleteRequestDto
import com.rangjin.kotlinblog.domain.user.dao.UserRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.lang.Exception

@Service
class CommentService (
    private val commentRepository: CommentRepository,

    private val articleRepository: ArticleRepository,

    private val userRepository: UserRepository,
){

    fun create(requestDto: CommentCreateOrUpdateRequestDto, articleId: Long): CommentCreateOrUpdateResponseDto {
        // todo: email, password 확인 절차 추가
        val user = userRepository.findByEmail(requestDto.email) ?: throw Exception("error")

        // todo: 존재 하지 않는 게시물 예외 처리 정리
        // todo: article 유효성 검사("", " ", null)
        val article = articleRepository.findByIdOrNull(articleId) ?: throw Exception("error")

        // todo: comment 유효성 검사("", " ", null)
        val comment = Comment(requestDto, article, user)
        return CommentCreateOrUpdateResponseDto(commentRepository.save(comment), user.email)
    }

    fun update(requestDto: CommentCreateOrUpdateRequestDto, articleId: Long, commentId: Long): CommentCreateOrUpdateResponseDto {
        // todo: 존재 하지 않는 사용자 예외 처리 정리
        // todo: email, password 확인 절차 추가
        val user = userRepository.findByEmail(requestDto.email) ?: throw Exception("error")

        // todo: 존재 하지 않는 게시물 예외 처리 정리
        // todo: article 유효성 검사("", " ", null)
        val article = articleRepository.findByIdOrNull(articleId) ?: throw Exception("error")

        // todo: 존재 하지 않는 댓글 예외 처리 정리
        // todo: comment 유효성 검사("", " ", null)
        val comment = commentRepository.findByIdOrNull(commentId) ?: throw Exception("error")
        comment.update(requestDto)

        return CommentCreateOrUpdateResponseDto(commentRepository.save(comment), user.email)
    }

    fun delete(requestDto: CommentDeleteRequestDto, articleId: Long, commentId: Long) {
        // todo: email, password 확인 절차 추가
        val user = userRepository.findByEmail(requestDto.email)

        val article = articleRepository.findByIdOrNull(articleId) ?: throw Exception("error")

        val comment = commentRepository.findByIdOrNull(commentId) ?: throw Exception("error")

        commentRepository.delete(comment)
    }

}